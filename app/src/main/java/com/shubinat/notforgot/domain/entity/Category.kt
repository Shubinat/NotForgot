package com.shubinat.notforgot.domain.entity

data class Category(
    val id: Int,
    val name: String,
    val user: User
)