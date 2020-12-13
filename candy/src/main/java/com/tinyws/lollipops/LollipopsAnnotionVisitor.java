package com.tinyws.lollipops;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import static com.tinyws.lollipops.LollipopsConstant.HELP_CLASS;

public class LollipopsAnnotionVisitor extends AdviceAdapter {

    private String className;
    private String methodName;
    private boolean inject = false;

    public LollipopsAnnotionVisitor(int access, MethodVisitor methodVisitor, String className, String methodName, String desc) {
        super(Opcodes.ASM6, methodVisitor, access, methodName, desc);
        this.className = className;
        this.methodName = methodName;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (Type.getDescriptor(Lollipops.class).equals(descriptor)) {
            inject = true;
        }
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        if (inject) {
            mv.visitLdcInsn(className);
            mv.visitLdcInsn(methodName);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, HELP_CLASS, "start", "(Ljava/lang/String;Ljava/lang/String;)V", false);
        }
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
        if (inject) {
            mv.visitLdcInsn(className);
            mv.visitLdcInsn(methodName);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, HELP_CLASS, "end", "(Ljava/lang/String;Ljava/lang/String;)V", false);
        }
    }
}