package org.devio.hi.library.restful

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap

/**
 * @Author yanyonghua
 * @Date 2022/11/3-13:54
 * @Des $.
 */
open class HiRestful constructor(val baseUrl: String,val callFactory: HiCall.Factory) {
    private var interceptors: MutableList<HiInterceptor> = mutableListOf()
    private var methodService :ConcurrentHashMap<Method,MethodParser> = ConcurrentHashMap()
    private var scheduler:Scheduler
    fun addInterceptor(interceptor: HiInterceptor) {
        interceptors.add(interceptor)
    }
    init {
         scheduler =Scheduler(callFactory,interceptors)
    }
    /**
     * interface ApiService {
     *  @Headers("auth-token:token", "accountId:123456")
     *  @BaseUrl("https://api.devio.org/as/")
     *  @POST("/cities/{province}")
     *  @GET("/cities")
     * fun listCities(@Path("province") province: Int,@Filed("page") page: Int): HiCall<JsonObject>
     * }
     */
    fun <T> create(service: Class<T>): T {
        return Proxy.newProxyInstance(
            service.classLoader,
            arrayOf<Class<*>>(service)
        ,object :InvocationHandler{
                override fun invoke(proxy: Any?, method: Method, args: Array<out Any>?): Any {
                    var methodParser = methodService.get(method)
                    if (methodParser == null){
                        methodParser=  MethodParser.parse(baseUrl, method)
                        methodService.put(method,methodParser)
                    }
                    //bugFix：此处 应当考虑到 methodParser复用，每次调用都应当解析入参
                    val newRequest = methodParser.newRequest(method,args)
//            callFactory.newCall(newRequest)
                   return scheduler.newCall(newRequest)
                }

            }
        ) as T
    }
}