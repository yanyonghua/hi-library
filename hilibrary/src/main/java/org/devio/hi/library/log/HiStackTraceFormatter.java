package org.devio.hi.library.log;


import java.util.Stack;

/**
 * @Author yanyonghua
 * @Date 2022/10/13-16:20
 * @Des $.
 */
public class HiStackTraceFormatter implements HiLogFormatter<StackTraceElement[]>{
    @Override
    public String format(StackTraceElement[] stackTrace) {
        StringBuilder sb = new StringBuilder(128);
        if (stackTrace == null ||stackTrace.length ==0){
            return null;
        }else if (stackTrace.length ==1){
            //有一个八位的缩进
            return "\t- "+stackTrace[0].toString();
        }else {
            for (int i = 0; i < stackTrace.length; i++) {
                if (i == 0){
                    sb.append("stackTrace:\n");
                }
                //不是最后一行
                if (i!= stackTrace.length-1){
                    sb.append("\t├ ");
                    sb.append(stackTrace[i].toString());
                    sb.append("\n");
                }else {
                    sb.append("\t└");
                    sb.append(stackTrace[i].toString());
                }
            }

        }
        return sb.toString();
    }
}
