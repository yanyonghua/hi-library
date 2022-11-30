package org.devio.hi.library.log;

import androidx.annotation.NonNull;

/**
 * @Author yanyonghua
 * @Date 2022/10/13-16:14
 * @Des $.
 */
public interface HiLogPrinter {
    void print(@NonNull HiLogConfig config, int level , String tag , @NonNull String printString);
}
