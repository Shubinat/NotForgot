package com.shubinat.notforgot.data.roomWithRetrofit.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.shubinat.notforgot.domain.entity.Priority

data class NoteModel(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("title")
    @Expose
    val title: String,
    @SerializedName("description")
    @Expose
    val description: String,
    @SerializedName("creation_date")
    @Expose
    val creationDate: String,
    @SerializedName("completion_date")
    @Expose
    val completionDate: String,
    @SerializedName("is_completed")
    @Expose
    val completed: Boolean,
    @SerializedName("category_id")
    @Expose
    val categoryId: Int,
    @SerializedName("priority")
    @Expose
    val priority: Priority
)