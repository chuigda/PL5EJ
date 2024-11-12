package tech.icey.pl5.drill;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public final class TestBytecodeGen {
    public static void main(String[] args) {
        var cw = new ClassWriter(0);
        // assume everything is imported
        // create a class named tech.icey.pl5.gen.Test
        cw.visit(
                Opcodes.V1_8,
                Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL,
                "tech/icey/pl5/gen/Test",
                null,
                "java/lang/Object",
                null
        );
        // add a static method add
        var mv = cw.visitMethod(
                Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC,
                "add",
                "(II)I",
                null,
                null
        );
        // add the method body
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ILOAD, 0);
        mv.visitVarInsn(Opcodes.ILOAD, 1);
        mv.visitInsn(Opcodes.IADD);
        mv.visitInsn(Opcodes.IRETURN);
        mv.visitMaxs(2, 2);
        mv.visitEnd();
        cw.visitEnd();

        // load the class
        var loader = new ClassLoader() {
            public Class<?> defineClass(byte[] b) {
                return defineClass(null, b, 0, b.length);
            }
        };

        var clazz = loader.defineClass(cw.toByteArray());
        try {
            var method = clazz.getMethod("add", int.class, int.class);
            System.out.println(method.invoke(null, 1, 2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
