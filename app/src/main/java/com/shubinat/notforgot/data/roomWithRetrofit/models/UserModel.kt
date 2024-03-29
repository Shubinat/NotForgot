package com.shubinat.notforgot.data.roomWithRetrofit.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("id")
    @Expose
    val id: Int,
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