package com.shubinat.notforgot.data.roomWithRetrofit.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access_token")
    @Expose
    val accessToken : String,
    @SerializedName("user")
    @Expose
    val user: UserModel
)