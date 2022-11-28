package com.shubinat.notforgot.domain.entity

data class User(
    val id: Int,
    val name: String,
    val login: String,
    val password: String
)