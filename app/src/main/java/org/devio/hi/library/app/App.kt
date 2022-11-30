package org.devio.hi.library.app

import android.app.Application
import com.google.gson.Gson
import org.devio.hi.library.log.HiConsolePrinter
import org.devio.hi.library.log.HiFilePrinter
import org.devio.hi.library.log.HiLogConfig
import org.devio.hi.library.log.HiLogManager

/**
 * @Author yanyonghua
 * @Date 2022/10/13-16:01
 * @Des $.
 */
class App:Application() {
    override fun onCreate() {
        super.onCreate()
        HiLogManager.init(object :HiLogConfig(){
            override fun injectJsonParser(): JsonParser {
                return JsonParser { src -> Gson().toJson(src) }
            }
            override fun getGlobalTag(): String {
                return "MyApp"
            }

            override fun enable(): Boolean {
                return true
            }
        },  HiConsolePrinter(),
            HiFilePrinter.getInstance(applicationContext.cacheDir.absolutePath, 0))
    }
}