package com.shubinat.notforgot.domain.entity


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.util.Date

@Parcelize
data class Note(
    var id: Int,
    val title: String,
    val description: String,
    val creationDate: LocalDate,
    val completionDate: LocalDate,
    val completed: Boolean,
    val category: Category?,
    val priority: Priority,
    val user: User
) : Parcelable {

    val shortDescription: String
        get() {
            if (description.length >= 25)
                return description.substring(0, 25) + "..."
            return description
        }
}