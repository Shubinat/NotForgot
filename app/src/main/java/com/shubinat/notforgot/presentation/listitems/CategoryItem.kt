package com.shubinat.notforgot.presentation.listitems

import com.shubinat.notforgot.domain.entity.Category

class CategoryItem(val category: Category) : NoteListItem() {
    override fun getType(): Int {
        return TYPE_CATEGORY
    }
}