package org.devio.hi.library.restful.annotation

/**
 * @POST("/cities/all")
 *fun test(@Filed("province") int provinceId)
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class POST(val value:String,val formPost:Boolean =true)
