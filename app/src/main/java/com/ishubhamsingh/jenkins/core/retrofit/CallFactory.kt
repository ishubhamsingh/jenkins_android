package com.ishubhamsingh.jenkins.core.retrofit

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by Shubham Singh on 14-07-2019.
 */

class CallFactory : CallAdapter.Factory() {

    companion object {
        @JvmStatic @JvmName("create")
        operator fun invoke() = CallFactory()
    }

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        if (Deferred::class.java != getRawType(returnType)) {
            return null
        }
        if (returnType !is ParameterizedType) {
            throw IllegalStateException(
                "Deferred return type must be parameterized as Deferred<Foo> or Deferred<out Foo>")
        }
        val responseType = getParameterUpperBound(0, returnType)

        val rawDeferredType = getRawType(responseType)
        return if (rawDeferredType == Response::class.java) {
            if (responseType !is ParameterizedType) {
                throw IllegalStateException(
                    "Response must be parameterized as Response<Foo> or Response<out Foo>")
            }
            RxCallAdapterWrapper<Any>(getParameterUpperBound(0, responseType))
        } else {
            RxCallAdapterWrapper<Any>(responseType)
        }
    }

    class RxCallAdapterWrapper<R>(private val responseType: Type) : CallAdapter<R, Deferred<R>> {


        override fun adapt(call: Call<R>): Deferred<R> {
            val deferred = CompletableDeferred<R>()

            deferred.invokeOnCompletion {
                if (deferred.isCancelled) {
                    call.cancel()
                }
            }

            call.enqueue(object : Callback<R> {
                override fun onFailure(call: Call<R>, t: Throwable) {
                    deferred.completeExceptionally(t)
                }

                override fun onResponse(call: Call<R>, response: Response<R>) {
                    if (response.isSuccessful) {
                        response.body()?.let { deferred.complete(it) }
                    } else {
                        deferred.completeExceptionally(HttpException(response))
                    }
                }
            })

            return deferred
        }

        override fun responseType() = responseType
    }
}