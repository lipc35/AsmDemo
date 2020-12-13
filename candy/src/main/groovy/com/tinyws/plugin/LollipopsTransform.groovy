package com.tinyws.plugin


import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils

import com.tinyws.ZipFileUtils
import com.tinyws.lollipops.LollipopsClassVisitor
import com.tinyws.lollipops.LollipopsConfigUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

class LollipopsTransform extends Transform {

    @Override
    String getName() {
        return "lollipops"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)

        //inputs中是传过来的输入流，其中有两种格式，一种是jar包格式一种是目录格式。
        def inputs = transformInvocation.getInputs()
        //获取到输出目录，最后将修改的文件复制到输出目录，这一步必须做不然编译会报错
        def outputProvider = transformInvocation.getOutputProvider()

        for (TransformInput input : inputs) {
            for (JarInput jarInput : input.getJarInputs()) {
                handleJarInputs(jarInput, outputProvider)
            }

            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                handleDirectoryInput(directoryInput, outputProvider)
            }
        }
    }

    /**
     * 处理Jar中的class文件
     */
    void handleJarInputs(JarInput jarInput, TransformOutputProvider outputProvider) {

        File dest = outputProvider.getContentLocation(
                jarInput.getName(),
                jarInput.contentTypes,
                jarInput.scopes,
                Format.JAR)
        FileUtils.copyFile(jarInput.file, dest)
        if (LollipopsConfigUtils.isOpen()) {
            weaveJarTask(jarInput.file, dest)
        }
    }

    private void weaveJarTask(File input, File output) {
        ZipOutputStream zipOutputStream = null
        ZipFile zipFile = null
        try {
            zipOutputStream = new ZipOutputStream(new BufferedOutputStream(Files.newOutputStream(output.toPath())))
            zipFile = new ZipFile(input)
            Enumeration<ZipEntry> enumeration = zipFile.entries()
            while (enumeration.hasMoreElements()) {
                ZipEntry zipEntry = enumeration.nextElement()
                String zipEntryName = zipEntry.name
                if (isMatchCondition(zipEntryName)) {
                    byte[] data = weaveSingleClassToByteArray(new BufferedInputStream(zipFile.getInputStream(zipEntry)))
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data)
                    ZipEntry newZipEntry = new ZipEntry(zipEntryName)
                    ZipFileUtils.addZipEntry(zipOutputStream, newZipEntry, byteArrayInputStream)
                } else {
                    InputStream inputStream = zipFile.getInputStream(zipEntry)
                    ZipEntry newZipEntry = new ZipEntry(zipEntryName)
                    ZipFileUtils.addZipEntry(zipOutputStream, newZipEntry, inputStream)
                }
            }
        } catch (Exception e) {
        } finally {
            try {
                if (zipOutputStream != null) {
                    zipOutputStream.finish()
                    zipOutputStream.flush()
                    zipOutputStream.close()
                }
                if (zipFile != null) {
                    zipFile.close()
                }
            } catch (Exception e) {
            }
        }
    }

    private boolean isMatchCondition(String name) {
        if (name.contains("R\$")) {
            return false
        }
        if (name.contains("R.class")) {
            return false
        }
        if (name.contains("BuildConfig.class")) {
            return false;
        }
        if (!name.endsWith(".class")) {
            return false;
        }
        return LollipopsConfigUtils.checkCanWave(name)
    }

    private byte[] weaveSingleClassToByteArray(InputStream inputStream) {
        ClassReader classReader = new ClassReader(inputStream)
        //对class文件的写入
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
        //访问class文件相应的内容，解析到某一个结构就会通知到ClassVisitor的相应方法
        ClassVisitor classVisitor = new LollipopsClassVisitor(classWriter)
        //依次调用 ClassVisitor接口的各个方法
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
        //toByteArray方法会将最终修改的字节码以 byte 数组形式返回。
        return classWriter.toByteArray()
    }

    /**
     * 处理文件目录下的class文件
     */
    void handleDirectoryInput(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {

        directoryInput.file.eachFileRecurse {
            File file ->
                if (file.isFile()) {
                    //对class文件进行读取与解析
                    ClassReader classReader = new ClassReader(file.bytes)
                    //对class文件的写入
                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    //访问class文件相应的内容，解析到某一个结构就会通知到ClassVisitor的相应方法
                    ClassVisitor classVisitor = new LollipopsClassVisitor(classWriter)
                    //依次调用 ClassVisitor接口的各个方法
                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
                    //toByteArray方法会将最终修改的字节码以 byte 数组形式返回。
                    byte[] bytes = classWriter.toByteArray()
                    //这个地址在javac目录下
                    FileOutputStream outputStream = new FileOutputStream(file.path)
                    outputStream.write(bytes)
                    outputStream.close()
                }
        }

        //Transform 拷贝文件到transforms目录
        File dest = outputProvider.getContentLocation(
                directoryInput.getName(),
                directoryInput.getContentTypes(),
                directoryInput.getScopes(),
                Format.DIRECTORY);
        //将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
        FileUtils.copyDirectory(directoryInput.getFile(), dest)
    }
}