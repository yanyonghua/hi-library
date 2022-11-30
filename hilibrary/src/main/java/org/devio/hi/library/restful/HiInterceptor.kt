package org.devio.hi.library.restful

/**
 * @Author yanyonghua
 * @Date 2022/11/3-11:54
 * @Des $.
 */
interface HiInterceptor{
    fun intercept(chain:Chain):Boolean

    /**
     * Chain对象会在我们派发拦截器的时候创建
     */
    interface Chain{
        val isRequestPeriod:Boolean get() = false

        fun request():HiRequest

        /**
         * 这个response对象在网络发起之前是为空的
         */
        fun response():HiResponse<*>?
    }
}