package org.devio.hi.library.restful

import java.io.IOException
import kotlin.jvm.Throws

/**
 * @Author yanyonghua
 * @Date 2022/11/3-11:45
 * @Des $.
 */
interface HiCall<T> {
    @Throws(IOException::class)
    fun execute():HiResponse<T>

    fun enqueue(callback: HiCallback<T>)
    interface Factory{
        fun newCall(request:HiRequest):HiCall<*>
    }
}