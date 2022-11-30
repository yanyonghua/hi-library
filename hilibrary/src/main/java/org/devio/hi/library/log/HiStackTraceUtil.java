package org.devio.hi.library.log;

/**
 * @Author yanyonghua
 * @Date 2022/10/14-11:57
 * @Des $.
 */
public class HiStackTraceUtil {
    public static StackTraceElement[] getCropRealStackTrace(StackTraceElement[] stackTrace, String ignorePackage, int maxDepth) {
        return cropStackTrace(getRealStackTrace(stackTrace, ignorePackage), maxDepth);
    }

    /**
     * 获取除忽略包之外的堆栈信息
     *
     * @param stackTrace
     * @param ignorePackage
     * @return
     */
    private static StackTraceElement[] getRealStackTrace(StackTraceElement[] stackTrace, String ignorePackage) {
        int ignoreDepth = 0;
        int allDepth = stackTrace.length;
        String className;
        for (int depth = allDepth - 1; depth >= 0; depth--) {
            className = stackTrace[depth].getClassName();
            if (ignorePackage != null && className.startsWith(ignorePackage)) {
                ignoreDepth = depth + 1;
                break;
            }
        }
        int realDepth = allDepth - ignoreDepth;
        StackTraceElement[] realStack = new StackTraceElement[realDepth];
        System.arraycopy(stackTrace, ignoreDepth, realStack, 0, realDepth);
        return realStack;
    }

    /**
     * 裁剪堆栈信息
     *
     * @param callStack
     * @param maxDepth
     * @return
     */
    private static StackTraceElement[] cropStackTrace(StackTraceElement[] callStack, int maxDepth) {
        int realDepth = callStack.length;
        if (maxDepth > 0) {
            realDepth = Math.min(maxDepth, realDepth);
        }
        StackTraceElement[] realStack = new StackTraceElement[realDepth];
        System.arraycopy(callStack, 0, realStack, 0, realDepth);
        return realStack;
    }
}
