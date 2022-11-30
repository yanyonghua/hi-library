package org.devio.hi.library.restful.annotation

/**
 * @Author yanyonghua
 * @Date 2022/11/16-13:28
 * @Des $.
 */
@Target(AnnotationTarget.FUNCTION,AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class CacheStrategy(val value: Int = NET_ONLY) {
    companion object {
        const val CACHE_FIRST = 0// 请求接口时候先读取本地缓存，在读取接口，接口成功后更新缓存 （页面初始化数据）
        const val NET_ONLY = 1 //仅仅请求接口（分页和独立页面）
        const val NET_CACHE = 2 //先接口，接口成功后更新缓存（一般是下拉刷新）
    }
}
