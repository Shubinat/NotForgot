package com.shubinat.notforgot.data.roomWithRetrofit.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
class Category(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String,
    @ColumnInfo(name = "user_id") val userId: Int
)
