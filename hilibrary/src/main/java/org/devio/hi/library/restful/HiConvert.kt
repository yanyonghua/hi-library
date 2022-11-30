package org.devio.hi.library.restful

import java.lang.reflect.Type

/**
 * @Author yanyonghua
 * @Date 2022/11/4-17:09
 * @Des $.
 */
interface HiConvert {
    fun <T> convert(rawData:String,dataType: Type):HiResponse<T>
}