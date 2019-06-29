package com.brins.lightmusic

import android.util.Log
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.ConcurrentHashMap


class RxBus {

    private val mStickyEventMap: MutableMap<Class<*>, Any> = ConcurrentHashMap()

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
        fun defaultSubscriber(): Observer<Any> {
            return object : Observer<Any> {
                override fun onComplete() {
                    Log.d(TAG, "Duty off!!!")
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Any) {
                    Log.d(TAG, "New event received: $t")
                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, "What is this? Please solve this as soon as possible!", e)
                }

            }
        }
    }

    private lateinit var dispoable: Disposable
    private val mEventBus = PublishSubject.create<Any>()

    fun post(event: Any) {
        mEventBus.onNext(event)
    }

    /**
     * 发送一个新Sticky事件
     */
    @Synchronized
    fun postSticky(event: Any) {
        run {
            mStickyEventMap.put(event.javaClass, event)
        }
        post(event)
    }

    @Synchronized
    fun toObservableSticky(classType: Class<Any>): Observable<Any> {
        val observable = mEventBus.ofType(classType)
        val obEvent = mStickyEventMap[classType]
        return if (obEvent != null) {
            observable.mergeWith(Observable.just(classType.cast(obEvent)))
        } else {
            observable
        }
    }

    fun toObservable(classType: Class<Any>): Observable<Any> {
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