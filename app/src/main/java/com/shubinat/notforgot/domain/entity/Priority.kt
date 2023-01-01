package com.shubinat.notforgot.domain.entity

import android.app.Application
import com.shubinat.notforgot.R

enum class Priority {
    EMPTY, LOW, MIDDLE, IMPORTANT, VERY_IMPORTANT;

    fun getStringValue(app: Application): String {
        return when (this) {
            EMPTY -> app.getString(R.string.priority_empty)
            LOW -> app.getString(R.string.priority_low)
            MIDDLE -> app.getString(R.string.priority_middle)
            IMPORTANT -> app.getString(R.string.priority_important)
            VERY_IMPORTANT -> app.getString(R.string.priority_very_important)
        }
    }

    companion object {
        fun parse(app: Application, value: String): Priority {
            return when (value) {
                app.getString(R.string.priority_empty) -> EMPTY
                app.getString(R.string.priority_low) -> LOW
                app.getString(R.string.priority_middle) -> MIDDLE
                app.getString(R.string.priority_important) -> IMPORTANT
                app.getString(R.string.priority_very_important) -> VERY_IMPORTANT
                else -> throw RuntimeException("Unknown string value: $value")
            }
        }
    }
}