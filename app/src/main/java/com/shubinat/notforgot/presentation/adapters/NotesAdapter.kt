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

class NotesAdapter(private val list : List<NoteListItem>) : RecyclerView.Adapter<BaseNoteViewHolder>() {

    var onItemCheckBoxClickListener: ((Note) -> Unit)? = null
    var onItemLongClickListener: ((Note) -> Unit)? = null


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
        if (item is NoteItem) {
            if (holder is NoteViewHolder){
                holder.binding.checkBoxCompleted.setOnClickListener {
                    onItemCheckBoxClickListener?.invoke(item.note)
                }
                holder.binding.root.setOnLongClickListener {
                    onItemLongClickListener?.invoke(item.note)
                    true
                }
            }
        }
    }

    override fun onViewRecycled(holder: BaseNoteViewHolder) {
        holder.clear()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}