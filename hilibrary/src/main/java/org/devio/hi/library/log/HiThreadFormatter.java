package org.devio.hi.library.log;

/**
 * @Author yanyonghua
 * @Date 2022/10/13-16:18
 * @Des $.
 */
public class HiThreadFormatter implements HiLogFormatter<Thread>{
    @Override
    public String format(Thread data) {
        return "Thread:"+ data.getName();
    }
}
