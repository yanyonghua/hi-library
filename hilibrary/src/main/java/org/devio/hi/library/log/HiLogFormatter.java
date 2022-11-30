package org.devio.hi.library.log;

/**
 * @Author yanyonghua
 * @Date 2022/10/13-16:16
 * @Des $.
 */
interface HiLogFormatter<T> {
    String format(T data);
}
