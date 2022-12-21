package com.shubinat.notforgot.presentation.viewholders

import com.shubinat.notforgot.R
import com.shubinat.notforgot.databinding.ItemNoteBinding
import com.shubinat.notforgot.domain.entity.Note
import com.shubinat.notforgot.domain.entity.Priority
import com.shubinat.notforgot.presentation.listitems.NoteItem
import com.shubinat.notforgot.presentation.listitems.NoteListItem

class NoteViewHolder(private val binding: ItemNoteBinding) : BaseNoteViewHolder(binding.root) {
    override fun bind(data: NoteListItem) {
        if (data is NoteItem) {
            with(binding) {
                textViewHeader.text = data.note.title
                textViewDescription.text = data.note.description
                checkBoxCompleted.isChecked = data.note.completed
                frameLayoutPriority.setBackgroundResource(
                    when (data.note.priority) {
                        Priority.LOW -> {
                            R.color.priority_low
                        }
                        Priority.MIDDLE -> {
                            R.color.priority_middle
                        }
                        Priority.IMPORTANT -> {
                            R.color.priority_important
                        }
                        Priority.VERY_IMPORTANT -> {
                            R.color.priority_very_important
                        }
                    }
                )
            }
        } else {
            throw RuntimeException("data is not NoteItem")
        }
    }

    override fun clear() {
        with(binding) {
            textViewHeader.text = ""
            textViewDescription.text = ""
            checkBoxCompleted.isChecked = false
            frameLayoutPriority.setBackgroundColor(binding.root.resources.getColor(R.color.white))
        }
    }

}