package com.brins.lightmusic.api

import android.util.Log
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.*
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.atomic.AtomicInteger

class BaseInterceptor(var logTag: String) : Interceptor {


    companion object {
        private val mCounter = AtomicInteger(0)
        private val MEDIA_TYPE_FORM =
            MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8")
        private val UTF_8 = Charset.forName("UTF-8")
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val countStr = String.format("【%d】", mCounter.incrementAndGet())
        var request = chain.request()
        val requestBuilder = request.newBuilder()
        val jsonParser = JsonParser()
        var requestInfo: JsonElement? = null
        val t1 = System.nanoTime()
        val costTimeStr: String
        val infoBody = bodyToString(request.body())
        requestInfo = jsonParser.parse(infoBody)
        val requestJson = JsonObject()
//        requestJson.add("phead", PheadCreator.getPhead())
        requestJson.add("info", requestInfo)
        val requestStr = requestJson.toString()
        Log.i(this.logTag, "=====Request===== $countStr ${request.url()}")
        Log.i(this.logTag, requestStr)
        val response = chain.proceed(request)
        val t2 = System.nanoTime()
        costTimeStr = "${(t2 - t1).toDouble() / 1000000.0}ms"
        Log.i(this.logTag, "=====Response===== result $countStr $costTimeStr")
        Log.i(this.logTag, response.body()!!.string())
        return response

    }

    private fun bodyToString(request: RequestBody?): String {
        try {
            val buffer = Buffer()
            if (request != null) {
                request.writeTo(buffer)
                return buffer.readUtf8()
            } else {
                return ""
            }
        } catch (var3: IOException) {
            return "did not work"
        }

    }
}