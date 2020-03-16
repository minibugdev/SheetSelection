package com.minibugdev.sheetselection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_selection_item.*

typealias OnItemSelectedListener = (item: SheetSelectionItem, position: Int) -> Unit

class SheetSelectionAdapter(
    private val source: List<SheetSelectionItem>,
    private val selectedPosition: Int,
    private val onItemSelectedListener: OnItemSelectedListener?
) : RecyclerView.Adapter<SheetSelectionAdapter.ViewHolder>() {

    private var items: List<SheetSelectionItem> = source

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
            item = items[position],
            position = position,
            selected = position == selectedPosition,
            onItemSelectedListener = onItemSelectedListener
        )
    }

    fun search(keyword: String?) {
        if (keyword.isNullOrBlank()) {
            updateItems(source)
        } else {
            val searchResult = source.filter { it.value.contains(keyword, true) }
            if (searchResult.isEmpty()) {
                updateItems(listOf(SheetSelectionItem("search_not_found", "Search not found.")))
            } else {
                updateItems(searchResult)
            }
        }
    }

    private fun updateItems(items: List<SheetSelectionItem>) {
        this.items = items
        notifyDataSetChanged()
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