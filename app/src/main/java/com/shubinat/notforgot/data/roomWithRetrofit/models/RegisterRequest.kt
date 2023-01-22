package com.shubinat.notforgot.data.roomWithRetrofit.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterRequest (
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("login")
    @Expose
    val login: String,
    @SerializedName("password")
    @Expose
    val password: String
)