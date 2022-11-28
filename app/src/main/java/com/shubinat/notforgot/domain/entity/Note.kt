package com.shubinat.notforgot.domain.entity

import java.util.Date

data class Note(
    val id: Int,
    val title: String,
    val description: String,
    val creationDate: Date,
    val completionDate: Date,
    val completed: Boolean,
    val category: Category,
    val priority: Priority,
    val user: User
)