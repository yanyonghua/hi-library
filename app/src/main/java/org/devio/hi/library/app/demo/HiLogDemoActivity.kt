package org.devio.hi.library.app.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import org.devio.hi.library.app.R
import org.devio.hi.library.log.*

class HiLogDemoActivity : AppCompatActivity() {
    var viewPrinter:HiViewPrinter?=null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hi_log_demo)
        viewPrinter = HiViewPrinter(this)
        findViewById<Button>(R.id.btn_log).setOnClickListener {
            printerLog()
        }
        viewPrinter!!.viewProvider.showFloatingView();
        //每次添加就会多一次
        HiLogManager.getInstance().addPrinter(viewPrinter)
    }
    private fun printerLog(){

        HiLog.log(object :HiLogConfig(){
            override fun includeTread(): Boolean {
                return true
            }

            override fun stackTraceDepth(): Int {
                return 0
            }

        },HiLogType.E,"----","5566")
        HiLog.a("9900")
    }
}