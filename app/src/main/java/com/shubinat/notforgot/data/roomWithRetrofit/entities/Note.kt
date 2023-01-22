package com.shubinat.notforgot.data.roomWithRetrofit.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )]
)
class Note(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val title: String,
    val description: String,
    @ColumnInfo(name = "creation_date") val creationDate: String,
    @ColumnInfo(name = "completion_date") val completionDate: String,
    val completed: Boolean,
    @ColumnInfo(name = "category_id") val categoryId: Int?,
    val priority: Int,
    @ColumnInfo(name = "user_id") val userId: Int
)
