package com.shubinat.notforgot.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class User (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val login: String,
    val password: String,
)