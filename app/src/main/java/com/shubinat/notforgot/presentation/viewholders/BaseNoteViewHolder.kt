package com.shubinat.notforgot.presentation.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.shubinat.notforgot.presentation.listitems.NoteListItem

abstract class BaseNoteViewHolder(view: View) : ViewHolder(view) {
    abstract fun bind(data: NoteListItem)
    abstract fun clear()
}