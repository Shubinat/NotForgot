package com.shubinat.notforgot.domain.entity

import android.app.Application
import android.os.Parcelable
import com.shubinat.notforgot.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    var id: Int,
    val name: String,
    val user: User
) : Parcelable {
    companion object {
        private const val NULL_CATEGORY_ID = -1
         fun getNullCategory(app: Application, authUser: User) : Category {
            return Category(
                Category.NULL_CATEGORY_ID,
                app.getString(R.string.null_category_name),
                authUser
            )
        }
    }
}