package org.devio.hi.library.log;

import android.util.Log;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Author yanyonghua
 * @Date 2022/10/13-15:30
 * @Des $.
 */
public class HiLogType {
    @IntDef({V,D,I,W,E,A})
    //注解保留时期源码级别
    @Retention(RetentionPolicy.SOURCE)
    public @interface TYPE{}
    public static final int V = Log.VERBOSE;
    public static final int D = Log.DEBUG;
    public static final int I = Log.INFO;
    public static final int W = Log.WARN;
    public static final int E = Log.ERROR;
    public static final int A = Log.ASSERT;

}
