package org.devio.hi.library.restful

import org.devio.hi.library.restful.annotation.*
import org.devio.hi.library.restful.annotation.CacheStrategy.Companion.NET_ONLY
import java.lang.IllegalStateException
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @Author yanyonghua
 * @Date 2022/11/3-14:01
 * @Des $.
 */
class MethodParser(val baseUrl: String, method: Method) {

    private var replaceRelativeUrl: String? = null
    private var cacheStrategy: Int = NET_ONLY
    private var domainUrl: String? = null
    private var formPost: Boolean = true
    private var httpMethod: Int = 0
    private lateinit var relativeUrl: String
    private var returnType: Type? = null
    private var headers: MutableMap<String, String> = mutableMapOf()
    private var parameters: MutableMap<String, String> = mutableMapOf()

    init {
        //parse method annotation such get headers post baseurl
        parseMethodAnnotations(method)
        //parse method parameters as path ,filed
//        parseMethodParameters(method, args)
        //parse method return type
        parseMethodReturnType(method)
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
    private fun parseMethodReturnType(method: Method) {
        if (method.returnType != HiCall::class.java) {
            throw IllegalStateException("method ${method.name} must be type of HiCall.class")
        }
        val type = method.genericReturnType
        if (type is ParameterizedType) {
            val typeArguments = type.actualTypeArguments
            require(typeArguments.size == 1) { "method %s can only has one generic return type" }
            returnType = typeArguments[0]
        } else {
            throw IllegalStateException(
                String.format(
                    "method %s must has one gerneric return type",
                    method.name
                )
            )
        }
    }

    /**
     * @Path("province") province: Int,@Filed("page") page: Int
     */
    private fun parseMethodParameters(method: Method, args: Array<Any>) {
        val parameterAnnotations = method.parameterAnnotations
        val size = parameterAnnotations.size
        val equals = size == args.size
        require(equals) {
            String.format(
                "arguments annotations count %s dont match expect count %s",
                size,
                args.size
            )
        }
        for (index in args.indices) {
            val annotations = parameterAnnotations[index]
            require(annotations.size <= 1) {
                "filed can only has one annotation : index = $index"
            }
            val value = args[index]
            require(isPrimitive(value)) { "8 basic types are supported for now ,index=$index " }
            val annotation = annotations[0]
            if (annotation is Filed) {
                val key = annotation.value
                val value = args[index]
                parameters[key] = value.toString()
            } else if (annotation is Path) {
                val replaceName = annotation.value
                val replacement = value.toString()
                if (replaceName != null && replacement != null) {
                    //relativeUrl ="home/{categoryId}"
                    replaceRelativeUrl = relativeUrl.replace("{$replaceName}", replacement)

                }
            } else if (annotation is CacheStrategy) {
                cacheStrategy = value as Int
            } else {
                throw IllegalStateException("cannot handle parameter annotation :" + annotation.javaClass.toString())
            }
        }
    }

    private fun isPrimitive(value: Any): Boolean {
        //String
        if (value.javaClass == String::class.java) {
            return true
        }
        try {
            // int byte short long boolean char double float
            val field = value.javaClass.getField("TYPE")
            val clazz = field[null] as Class<*>
            if (clazz.isPrimitive) {
                return true
            }
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NoSuchFileException) {
            e.printStackTrace()
        }
        return false
    }

    private fun parseMethodAnnotations(method: Method) {
        val annotations = method.annotations
        for (annotation in annotations) {
            if (annotation is GET) {
                relativeUrl = annotation.value;
                httpMethod = HiRequest.METHOD.GET
            } else if (annotation is POST) {
                relativeUrl = annotation.value;
                httpMethod = HiRequest.METHOD.POST
                formPost = annotation.formPost
            } else if (annotation is PUT) {
                formPost = annotation.formPost
                httpMethod = HiRequest.METHOD.PUT
                relativeUrl = annotation.value
            } else if (annotation is DELETE) {
                httpMethod = HiRequest.METHOD.DELETE
                relativeUrl = annotation.value
            } else if (annotation is Headers) {
                val headersArray = annotation.value;
                //@Headers("auth-token:token", "accountId:123456")
                for (header in headersArray) {
                    val colon = header.indexOf(":")
                    check(!(colon == 0 || colon == -1)) {
                        String.format(
                            "@Headers value must be in the form [name:value],but found [%s]",
                            header
                        )
                    }
                    val name = header.substring(0, colon)
                    val value = header.substring(colon + 1).trim()
                    headers[name] = value
                }
            } else if (annotation is BaseUrl) {
                domainUrl = annotation.value
            } else if (annotation is CacheStrategy) {
                cacheStrategy = annotation.value
            } else {
                throw IllegalStateException("cannot handle method annotation: " + annotation.javaClass.toString())
            }

        }
        //不满足的时候会抛出异常
        require((httpMethod == HiRequest.METHOD.GET)
                || (httpMethod == HiRequest.METHOD.POST)
                || (httpMethod == HiRequest.METHOD.PUT)
                || (httpMethod == HiRequest.METHOD.DELETE)) {
            throw IllegalStateException(
                String.format(
                    "method %s must has one of GET,POST,PUT,DELETE",
                    method.name
                )
            )
        }
        if (domainUrl == null) {
            domainUrl = baseUrl
        }
    }

    fun newRequest(method: Method, args: Array<out Any>?): HiRequest {
        val arguments: Array<Any> = args as Array<Any>? ?: arrayOf()
        parseMethodParameters(method, arguments)
        var request = HiRequest()
        request.domainUrl = domainUrl;
        request.returnType = returnType
        request.relativeUrl = replaceRelativeUrl ?: relativeUrl
        request.parameters = parameters
        request.headers = headers
        request.httpMethod = httpMethod
        request.formPost = formPost
        request.cacheStrategy = cacheStrategy
        return request
    }

    companion object {
        fun parse(baseUrl: String, method: Method): MethodParser {
            return MethodParser(baseUrl, method)
        }
    }
}