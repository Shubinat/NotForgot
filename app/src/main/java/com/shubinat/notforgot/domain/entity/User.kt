package com.shubinat.notforgot.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: Int,
    val name: String,
    val login: String,
    val password: String
) : Parcelable