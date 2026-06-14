package org.openkoala.koala.monitor.toolkit.asm;


/**
 * 编码访问控制符
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.5
 * @date  Oct 20, 2007
 */
public final class Modifier {

    public static final int  PUBLIC                      = java.lang.reflect.Modifier.PUBLIC;
    public static final int  PRIVATE                     = java.lang.reflect.Modifier.PRIVATE;
    public static final int  PROTECTED                   = java.lang.reflect.Modifier.PROTECTED;
    public static final int  STATIC                      = java.lang.reflect.Modifier.STATIC;
    public static final int  FINAL                       = java.lang.reflect.Modifier.FINAL;
    public static final int  NATIVE                      = java.lang.reflect.Modifier.NATIVE;

    public static final int  PUBLIC_STATIC               = PUBLIC | STATIC;
    public static final int  PUBLIC_STATIC_FINAL         = PUBLIC_STATIC | FINAL;
    public static final int  PRIVATE_STATIC              = PRIVATE | STATIC;
    public static final int  PRIVATE_STATIC_FINAL        = PRIVATE_STATIC | FINAL;
    private static final int NO_PUBLIC_PRIVATE_PROTECTED = ~(PRIVATE | PUBLIC | PROTECTED);

    private Modifier() {
    }

    public static boolean isInterface(int modifier) {
        return java.lang.reflect.Modifier.isInterface(modifier);
    }

    public static boolean isAbstract(int modifier) {
        return java.lang.reflect.Modifier.isAbstract(modifier);
    }

    public static boolean isNative(int modifier) {
        return java.lang.reflect.Modifier.isNative(modifier);
    }

    public static boolean isStatic(int modifier) {
        return java.lang.reflect.Modifier.isStatic(modifier);
    }

    public static int makePrivate(int modifier) {
        return (modifier & NO_PUBLIC_PRIVATE_PROTECTED) | PRIVATE;
    }


    public static int makePublic(int modifier) {
        return (modifier & NO_PUBLIC_PRIVATE_PROTECTED) | PUBLIC;
    }


    public static int makeProtected(int modifier) {
        return (modifier & NO_PUBLIC_PRIVATE_PROTECTED) | PROTECTED;
    }


    public static int makeNonNative(int modifier) {
        return (modifier & ~(NATIVE));
    }
}
