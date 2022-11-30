package org.devio.hi.library.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import org.devio.hi.library.app.demo.ExecutorDemoActivity
import org.devio.hi.library.app.demo.HiLogDemoActivity
import org.devio.hi.library.log.HiLog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun onClick(view: View) {
        when(view!!.id){
            R.id.tv_hilog->{
                startActivity(Intent(this,HiLogDemoActivity::class.java))
            }
            R.id.tv_hiexecutor->{
                startActivity(Intent(this, ExecutorDemoActivity::class.java))
            }
        }
    }

}