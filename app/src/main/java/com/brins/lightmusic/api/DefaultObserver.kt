package com.brins.lightmusic.api

import android.util.Log
import com.brins.lightmusic.R
import com.brins.lightmusic.utils.show
import com.google.gson.JsonParseException
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.json.JSONException
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException
import java.text.ParseException

abstract class DefaultObserver<T> : Observer<T> {
    override fun onComplete() {
    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onNext(response: T) {
        onSuccess(response)
        onFinish()
    }

    override fun onError(e: Throwable) {
        Log.d("Retrofit", e.message)
        if (e is HttpException) run { onException(ExceptionReason.BAD_NETWORK) }
        else if (e is ConnectException || e is UnknownHostException) {
            onException(ExceptionReason.CONNECT_ERROR)
        } else if (e is InterruptedIOException) {
            onException(ExceptionReason.CONNECT_TIMEOUT)
        } else if (e is JsonParseException
            || e is JSONException
            || e is ParseException
        ) {   //  解析错误
            onException(ExceptionReason.PARSE_ERROR)
        } else {
            onException(ExceptionReason.UNKNOWN_ERROR)
        }
        onFinish()
    }

    /**
     * 请求成功
     * onSuccess
     *
     * @param response 服务器返回的数据
     */
    abstract fun onSuccess(response: T)

    /**
     * 服务器返回数据，但响应码不为200
     */
    fun onFail(message: String) {
        show(message)
    }

    abstract fun onFinish()

    /**
     * 请求异常
     *
     * @param reason
     */
    fun onException(reason: ExceptionReason) {
        when (reason) {
            ExceptionReason.CONNECT_ERROR -> show(R.string.connect_error)

            ExceptionReason.CONNECT_TIMEOUT -> show(R.string.connect_timeout)

            ExceptionReason.BAD_NETWORK -> show(R.string.bad_network)

            ExceptionReason.PARSE_ERROR -> show(R.string.parse_error)

            ExceptionReason.UNKNOWN_ERROR -> show(R.string.unknown_error)
            else -> show(R.string.unknown_error)
        }
    }

    /**
     * 请求网络失败原因
     */
    enum class ExceptionReason {
        /**
         * 解析数据失败
         */
        PARSE_ERROR,
        /**
         * 网络问题
         */
        BAD_NETWORK,
        /**
         * 连接错误
         */
        CONNECT_ERROR,
        /**
         * 连接超时
         */
        CONNECT_TIMEOUT,
        /**
         * 未知错误
         */
        UNKNOWN_ERROR
    }
}