package org.devio.hi.library.log;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @Author yanyonghua
 * @Date 2022/10/14-12:35
 * @Des $.
 */
public class HiLogMo {
    public static SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.CHINA);
    public long timeMillis;
    public int level;
    public String tag;
    public String log;

    public HiLogMo(long timeMillis, int level, String tag, String log) {
        this.timeMillis = timeMillis;
        this.level = level;
        this.tag = tag;
        this.log = log;
    }

    public String getFlattened(){
        return format(timeMillis)+'|'+level+'|'+tag+'|';
    }
    public String flattenedLog(){
        return getFlattened()+"\n"+log;
    }

    private String format(long timeMillis) {
        return sdf.format(timeMillis);
    }
}
