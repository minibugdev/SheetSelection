package com.minibugdev.sheetselection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_selection_item.*

typealias OnItemSelectedListener = (item: SheetSelectionItem, position: Int) -> Unit

class SheetSelectionAdapter(
    private val items: List<SheetSelectionItem>,
    private val selectedPosition: Int,
    private val onItemSelectedListener: OnItemSelectedListener?
) : RecyclerView.Adapter<SheetSelectionAdapter.ViewHolder>() {

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.row_selection_item, parent, false)
            .run {
                ViewHolder(this)
            }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindView(
            items[position],
            position,
            position == selectedPosition,
            onItemSelectedListener
        )
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun onBindView(
            item: SheetSelectionItem, position: Int, selected: Boolean,
            onItemSelectedListener: OnItemSelectedListener?
        ) {
            val selectedIcon = if (selected) R.drawable.ic_check else 0
            textViewItem.setCompoundDrawablesWithIntrinsicBounds(item.icon ?: 0, 0, selectedIcon, 0)
            textViewItem.text = item.value

            textViewItem.setOnClickListener {
                onItemSelectedListener?.invoke(item, position)
            }
        }
    }
}