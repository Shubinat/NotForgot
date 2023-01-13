package com.shubinat.notforgot.data.roomWithRetrofit.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class User (
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String,
    val login: String,
    val password: String,
)