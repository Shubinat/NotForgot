package com.shubinat.notforgot.data.roomWithRetrofit.utils

import com.shubinat.notforgot.data.roomWithRetrofit.exceptions.BadRequestException
import com.shubinat.notforgot.data.roomWithRetrofit.exceptions.NotFoundException
import com.shubinat.notforgot.data.roomWithRetrofit.exceptions.UnauthorizedException
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("UNCHECKED_CAST")
suspend fun <T> Call<T>.convertToSuspend() = suspendCancellableCoroutine { cont ->
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            when (response.code()) {
                400 -> cont.resumeWith(Result.failure(BadRequestException()))
                404 -> cont.resumeWith(Result.failure(NotFoundException()))
                401 -> cont.resumeWith(Result.failure(UnauthorizedException()))
                else -> {
                    if (response.isSuccessful) {
                        cont.resumeWith(Result.success(response.body() as T))
                    } else {
                        cont.resumeWith(Result.failure(RuntimeException("Code is ${response.code()}")))
                    }
                }
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            cont.resumeWith(Result.failure(t))
        }

    })
    cont.invokeOnCancellation { this.cancel() }
}