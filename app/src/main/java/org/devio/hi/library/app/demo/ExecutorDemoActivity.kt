package org.devio.hi.library.app.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_executor_demo.*
import org.devio.hi.library.app.R
import org.devio.hi.library.executor.HiExecutor
import org.devio.hi.library.executor.HiExecutor.Callable

class ExecutorDemoActivity : AppCompatActivity() {
    private var paused: Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_executor_demo)

        priority.setOnClickListener {
            for (priority in 0 until 10) {
                HiExecutor.executor(priority, {
                    try {
                        Thread.sleep((1000 - priority * 100).toLong())
                    } catch (e: Exception) {

                    }
                }
                )
            }
        }
        pause_resume.setOnClickListener {
            if (paused){
                HiExecutor.resume()
            }else{
                HiExecutor.pause()
            }
            paused = !paused
        }

        async.setOnClickListener {
            HiExecutor.executor(0,object : Callable<String>() {
                override fun onBackground(): String {
                    Log.e("ExecutorDemoActivity", "onBackground: 当前线程是"+Thread.currentThread().name )
                    return "我是异步任务的结果"
                }

                override fun onCompleted(t: String) {
                    Log.e("ExecutorDemoActivity", "onCompleted: 当前线程是"+Thread.currentThread().name )
                    Log.e("ExecutorDemoActivity", "onCompleted: result  -->"+t )
                }

            })
        }
    }
}