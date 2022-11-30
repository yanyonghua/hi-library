package org.devio.hi.library.log;

/**
 * @Author yanyonghua
 * @Date 2022/10/13-15:40
 * @Des $.
 */
public abstract class HiLogConfig {
    static int MAX_LEN = 512;
    static HiThreadFormatter HI_THREAD_FORMATTER = new HiThreadFormatter();
    static HiStackTraceFormatter HI_STACK_TRACE_FORMATTER = new HiStackTraceFormatter();
    public JsonParser injectJsonParser(){
        return null;
    }

    public String getGlobalTag(){
        return "HiLog";
    }
    public boolean enable(){
        return true;
    }
    public HiLogPrinter[] printers(){
        return null;
    }
    public interface JsonParser{
        String toJson(Object src);
    }

    public boolean includeTread(){
        return false;
    }
    public int stackTraceDepth(){
        return 5;
    }

}
