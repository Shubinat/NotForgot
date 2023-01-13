package com.shubinat.notforgot.data.roomWithRetrofit.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class LoginRequest(
    @SerializedName("login")
    @Expose
    val login: String,
    @SerializedName("password")
    @Expose
    val password: String
)