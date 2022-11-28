package com.shubinat.notforgot.domain.entity

data class Category(
    var id: Int,
    val name: String,
    val user: User
)