package com.shubinat.notforgot.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.shubinat.notforgot.domain.entity.Category
import com.shubinat.notforgot.domain.entity.Priority
import com.shubinat.notforgot.domain.entity.User
import java.time.LocalDate

@Entity(
    foreignKeys = [
        ForeignKey(
        entity = com.shubinat.notforgot.data.room.entity.User::class,
        parentColumns = ["id"],
        childColumns = ["user_id"]
    ),
        ForeignKey(
            entity = com.shubinat.notforgot.data.room.entity.Category::class,
            parentColumns = ["id"],
            childColumns = ["category_id"]
        )]
)
class Note(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val description: String,
    @ColumnInfo(name = "creation_date") val creationDate: String,
    @ColumnInfo(name = "completion_date") val completionDate: String,
    val completed: Boolean,
    @ColumnInfo(name = "category_id") val categoryId: Int?,
    val priority: Int,
    @ColumnInfo(name = "user_id") val userId: Int
)
