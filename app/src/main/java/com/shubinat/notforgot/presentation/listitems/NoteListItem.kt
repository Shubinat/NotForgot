package com.shubinat.notforgot.presentation.listitems

abstract class NoteListItem {
    companion object {
        const val TYPE_CATEGORY = 0
        const val TYPE_NOTE = 1
    }

    abstract fun getType() : Int
}