package com.shubinat.notforgot.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shubinat.notforgot.databinding.ItemNoteBinding
import com.shubinat.notforgot.databinding.ItemNoteHeaderBinding
import com.shubinat.notforgot.domain.entity.Category
import com.shubinat.notforgot.domain.entity.Note
import com.shubinat.notforgot.domain.entity.Priority
import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.presentation.listitems.CategoryItem
import com.shubinat.notforgot.presentation.listitems.NoteItem
import com.shubinat.notforgot.presentation.listitems.NoteListItem
import com.shubinat.notforgot.presentation.viewholders.BaseNoteViewHolder
import com.shubinat.notforgot.presentation.viewholders.NoteHeaderViewHolder
import com.shubinat.notforgot.presentation.viewholders.NoteViewHolder
import java.util.Date

class NotesAdapter : RecyclerView.Adapter<BaseNoteViewHolder>() {

    private val list = listOf(
        CategoryItem(Category(1, "Работа", User(1, "", "", ""))),
        NoteItem(
            Note(
                1,
                "Погладить котика",
                "Нежно",
                Date(2022, 10, 2),
                Date(2022, 10, 2),
                true,
                null,
                Priority.LOW,
                User(1, "", "", "")
            )
        ),
        NoteItem(
            Note(
                1,
                "Сделать уроки",
                "Быстро",
                Date(2022, 10, 2),
                Date(2022, 10, 2),
                false,
                null,
                Priority.MIDDLE,
                User(1, "", "", "")
            )
        ),
        CategoryItem(Category(1, "Учеба", User(1, "", "", ""))),
        NoteItem(
            Note(
                1,
                "Покормить собаку",
                "GO! GO! GO!",
                Date(2022, 10, 2),
                Date(2022, 10, 2),
                false,
                null,
                Priority.VERY_IMPORTANT,
                User(1, "", "", "")
            )
        )

    )

    override fun getItemViewType(position: Int): Int {
        return list[position].getType()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseNoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            NoteListItem.TYPE_CATEGORY -> {
                val binding = ItemNoteHeaderBinding.inflate(inflater, parent, false)
                NoteHeaderViewHolder(binding)
            }
            NoteListItem.TYPE_NOTE -> {
                val binding = ItemNoteBinding.inflate(inflater, parent, false)
                NoteViewHolder(binding)
            }
            else -> throw RuntimeException("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: BaseNoteViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun onViewRecycled(holder: BaseNoteViewHolder) {
        holder.clear()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}