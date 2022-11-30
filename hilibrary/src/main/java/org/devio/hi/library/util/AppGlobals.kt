package org.devio.hi.library.util

import android.app.Application

/**
 * @Author yanyonghua
 * @Date 2022/11/6-23:24
 * @Des $.
 */
object AppGlobals {
    var application:Application?=null
    fun get():Application?{
        if (application ==null){
            try {
                application =    Class.forName("android.app.ActivityThread")
                    .getMethod("currentApplication")
                    .invoke(null) as Application
            }catch (ex:Exception){
                ex.printStackTrace()
            }
        }
        return application
    }
}