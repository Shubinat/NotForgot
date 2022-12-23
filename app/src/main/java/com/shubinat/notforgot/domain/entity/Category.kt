package com.shubinat.notforgot.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    var id: Int,
    val name: String,
    val user: User
) : Parcelable