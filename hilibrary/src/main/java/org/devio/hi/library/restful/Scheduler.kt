package org.devio.hi.library.restful

import android.view.View
import org.devio.hi.library.cache.HiStorage
import org.devio.hi.library.executor.HiExecutor
import org.devio.hi.library.log.HiLog
import org.devio.hi.library.restful.annotation.CacheStrategy
import org.devio.hi.library.util.MainHandler

/**
 * @Author yanyonghua
 * @Date 2022/11/3-15:22
 * @Des $.代理CallFactory创建出来的call对象，从而实现拦截器的派发动作
 */
class Scheduler(
    private val callFactory: HiCall.Factory, private val interceptors: MutableList<HiInterceptor>
) {
    fun newCall(newRequest: HiRequest): HiCall<*> {
        val newCall = callFactory.newCall(newRequest)
        return ProxyCall(newCall, newRequest)
    }

    internal inner class ProxyCall<T>(val delegate: HiCall<T>, val request: HiRequest) : HiCall<T> {
        override fun execute(): HiResponse<T> {
            dispatchInterceptor(request, null)
            if (request.cacheStrategy == CacheStrategy.CACHE_FIRST){
                val cacheResonse =readCache<T>()
                if (cacheResonse.data!=null){
                    return cacheResonse
                }
            }
            val response = delegate.execute()
            saveCacheIfNeed(response)
            dispatchInterceptor(request, response)
            return response
        }


        override fun enqueue(callback: HiCallback<T>) {
            dispatchInterceptor(request, null)
            if (request.cacheStrategy == CacheStrategy.CACHE_FIRST) {
                HiExecutor.executor(runnable = Runnable {
                    val cacheResponse = readCache<T>()
                    if (cacheResponse.data != null) {
                        MainHandler.sendAtFrontOfQueue(runnable = {
                            callback.onSuccess(cacheResponse)
                        })
                        HiLog.d("enqueue ,cache :" + request.getCacheKey())
                    }
                })
            }

            delegate.enqueue(object : HiCallback<T> {
                override fun onSuccess(response: HiResponse<T>) {
                    dispatchInterceptor(request, response)

                    saveCacheIfNeed(response)
                    if (callback != null) {
                        callback.onSuccess(response)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    if (callback != null) {
                        callback.onFailed(throwable)
                    }
                }

            })
        }

        private fun saveCacheIfNeed(response: HiResponse<T>) {
            if (request.cacheStrategy == CacheStrategy.CACHE_FIRST ||
                request.cacheStrategy == CacheStrategy.NET_CACHE) {
                if (response.data!=null){
                    HiExecutor.executor(runnable = Runnable {
                        HiStorage.saveCache(request.getCacheKey(),response.data)
                    })
                }
            }

        }

        private fun <T> readCache(): HiResponse<T> {
            //historage 查询缓存 需要提供一个cache key
            // request 的 url + 参数 或者可以 添加一个key
            val cacheKey = request.getCacheKey()
            val cache = HiStorage.getCache<T>(cacheKey)
            val cacheResponse = HiResponse<T>()
            cacheResponse.data = cache
            cacheResponse.code = HiResponse.CACHE_SUCCESS
            cacheResponse.msg = "缓存获取成功"
            return cacheResponse
        }

        private fun dispatchInterceptor(request: HiRequest, response: HiResponse<T>?) {
            InterceptorChain(request, response).dispatch()
        }

        internal inner class InterceptorChain(
            val request: HiRequest,
            val response: HiResponse<T>?
        ) : HiInterceptor.Chain {
            //代表的是分发的第几个拦截器
            var callIndex: Int = 0
            override val isRequestPeriod: Boolean
                get() = response == null

            override fun request(): HiRequest {
                return request
            }

            override fun response(): HiResponse<*>? {
                return response
            }

            fun dispatch() {
                val interceptor = interceptors[callIndex]
                val intercept = interceptor.intercept(this)
                callIndex++;
                if (!intercept && callIndex < interceptors.size) {
                    dispatch()
                }
            }

        }

    }
}