package com.tinyws.lollipops;

import com.tinyws.PluginConfig;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

public class LollipopsClassVisitor extends ClassVisitor {

    private String className;

    public LollipopsClassVisitor(ClassVisitor cv) {
        /**
         * 参数1：ASM API版本，源码规定只能为4，5，6
         * 参数2：ClassVisitor不能为 null
         */
        super(Opcodes.ASM6, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.className = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if (LollipopsConfigUtils.isOpen()) {
            if (LollipopsConfigUtils.checkCanWave(className)) {
                return new LollipopsMethodVisitor(access, mv, className, name, desc);
            } else {
                return new LollipopsAnnotionVisitor(access, mv, className, name, desc);
            }
        }
        return mv;
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}