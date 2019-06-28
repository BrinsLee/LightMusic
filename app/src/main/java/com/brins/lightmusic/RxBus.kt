package com.brins.lightmusic

import android.util.Log
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor
import io.reactivex.subjects.PublishSubject
import org.reactivestreams.Subscriber
import io.reactivex.internal.disposables.DisposableHelper.dispose
import io.reactivex.internal.disposables.DisposableHelper.isDisposed



class RxBus {

    companion object {
        @JvmStatic
        private val TAG = "RxBus"
        @JvmStatic
        @Volatile
        private var sInstance: RxBus? = null

        fun getInstance(): RxBus {
            if (sInstance == null) {
                synchronized(RxBus::class.java) {
                    if (sInstance == null) {
                        sInstance = RxBus()
                    }
                }
            }
            return sInstance!!
        }

        @JvmStatic
        fun defaultSubscriber() : Observer<Any>{
            return object : Observer<Any>{
                override fun onComplete() {
                    Log.d(TAG, "Duty off!!!")
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Any) {
                    Log.d(TAG, "New event received: $t")                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, "What is this? Please solve this as soon as possible!", e)
                }

            }
        }
    }

    private lateinit var dispoable : Disposable
    private val mEventBus  = PublishSubject.create<Any>()

    fun post(event: Any) {
        mEventBus.onNext(event)
    }

    fun toObservable(classType : Class<Any>): Observable<Any> {
        return mEventBus.ofType(classType)
    }

    /**
     * 订阅
     * @param bean
     * @param consumer
     */
    fun subscribe(bean: Class<Any>, consumer: Consumer<Any>) {
        dispoable = toObservable(bean).subscribe(consumer)
    }

    /**
     * 取消订阅
     */
    fun unSubcribe() {
        if (dispoable != null && dispoable.isDisposed) {
            dispoable.dispose()
        }

    }
}