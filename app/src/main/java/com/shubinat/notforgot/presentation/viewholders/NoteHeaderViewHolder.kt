package com.shubinat.notforgot.presentation.viewholders

import com.shubinat.notforgot.R
import com.shubinat.notforgot.databinding.ItemNoteHeaderBinding
import com.shubinat.notforgot.domain.entity.Category
import com.shubinat.notforgot.presentation.listitems.CategoryItem
import com.shubinat.notforgot.presentation.listitems.NoteListItem

class NoteHeaderViewHolder(private val binding: ItemNoteHeaderBinding) :
    BaseNoteViewHolder(binding.root) {

    override fun bind(data: NoteListItem) {
        if (data is CategoryItem) {
            if (data.category != null){
                binding.textViewHeader.setText(data.category.name)
            } else{
                binding.textViewHeader.setText(R.string.null_category_name)
            }

        } else {
            throw RuntimeException("data is not CategoryItem")
        }
    }

    override fun clear() {
        binding.textViewHeader.text = ""
    }

}