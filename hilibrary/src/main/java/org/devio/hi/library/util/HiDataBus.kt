package org.devio.hi.library.util

import androidx.lifecycle.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @Author yanyonghua
 * @Date 2022/11/13-16:40
 * @Des $.消息总线
 */
object HiDataBus {
    private val eventMap =ConcurrentHashMap<String,StickyLiveData<*>>()
    fun <T>with(eventName:String):StickyLiveData<T>{
        //基于事件名称订阅、分发消息，
        //由于一个 LiveData 只能发送 一种数据类型
        //所以 不同的event事件，需要使用不同的livedata实例去分发
        var liveData = eventMap[eventName]
        if (liveData==null){
            liveData = StickyLiveData<T>(eventName)
            eventMap[eventName] = liveData
        }
        return liveData as StickyLiveData<T>
    }

    class StickyLiveData<T>(private val eventName: String):LiveData<T>(){
         var mStickyData :T?=null
         var mVersion = 0
        fun setStickyData(stickyData: T){
            mStickyData =stickyData;
            setValue(stickyData)
            //就是只能在主线程发送数据
        }
        fun postStickeyData(stickyData: T){
            mStickyData =stickyData;
            postValue(stickyData)
            //不受线程限制
        }

        override fun setValue(value: T) {
            mVersion++
            super.setValue(value)
        }

        override fun postValue(value: T) {
            mVersion++
            super.postValue(value)
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            observerStickey(owner,false,observer)
        }

        fun observerStickey(owner: LifecycleOwner,sticky:Boolean,observer: Observer<in T>){
            //允许指定注册的观察者是否需要关心黏性事件
            //sticky = true ,如果之前存在已经发送的数据，那么这个observer会受到之前的黏性事件消息
            owner.lifecycle.addObserver(LifecycleEventObserver{source, event ->
                //监听 宿主发生销毁事件，主动把livedata移除掉
                if (event==Lifecycle.Event.ON_DESTROY){
                    eventMap.remove(eventName)
                }
            })
            super.observe(owner,StickyObserver(this,sticky,observer))
        }
    }

    class StickyObserver<T>(
       val stickyLiveData: StickyLiveData<T>,
       val sticky: Boolean,
        val observer: Observer<in T>
    ) : Observer<T> {
        //lastVersion和livedata的version对齐的原因，就是为了控制黏性事件的分发
        //sticky 不等于true ，只能接受到注册之后发送的消息，如果要接收黏性事件，则 sticky 需要设置为true
        private var lastVersion = stickyLiveData.mVersion
        override fun onChanged(t: T) {
            if (lastVersion >=stickyLiveData.mVersion ){
                //那就说明stickyLiveData 没有更新的数据需要发送
                if (sticky && stickyLiveData.mStickyData!=null){
                    observer.onChanged(stickyLiveData.mStickyData)
                }
                return
            }
            lastVersion = stickyLiveData.mVersion
            observer.onChanged(t)
        }
    }
}