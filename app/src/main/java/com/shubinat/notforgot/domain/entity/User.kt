package com.shubinat.notforgot.domain.entity

data class User(
    var id: Int,
    val name: String,
    val login: String,
    val password: String
)