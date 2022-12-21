package com.shubinat.notforgot.presentation.listitems

import com.shubinat.notforgot.domain.entity.Note

class NoteItem(val note: Note) : NoteListItem() {
    override fun getType(): Int {
        return TYPE_NOTE
    }
}