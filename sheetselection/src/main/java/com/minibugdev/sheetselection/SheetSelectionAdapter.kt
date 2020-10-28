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
    private val searchNotFoundText: String,
    private val onItemSelectedListener: OnItemSelectedListener?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<SheetSelectionItem> = source

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int =
        when (items[position].key) {
            KEY_SEARCH_NOT_FOUND -> R.layout.row_empty_item
            else -> R.layout.row_selection_item
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.row_selection_item -> ItemViewHolder(view)
            R.layout.row_empty_item -> EmptyViewHolder(view, searchNotFoundText)
            else -> throw IllegalAccessException("Item view type doesn't match.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.onBindView(
                item = items[position],
                position = position,
                onItemSelectedListener = onItemSelectedListener
            )
        }
    }

    fun search(keyword: String?) {
        if (keyword.isNullOrBlank()) {
            updateItems(source)
        } else {
            val searchResult = source.filter { it.value.contains(keyword, true) }
            if (searchResult.isEmpty()) {
                updateItems(
                    listOf(
                        SheetSelectionItem(KEY_SEARCH_NOT_FOUND, searchNotFoundText)
                    )
                )
            } else {
                updateItems(searchResult)
            }
        }
    }

    private fun updateItems(items: List<SheetSelectionItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    class ItemViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun onBindView(
            item: SheetSelectionItem,
            position: Int,
            onItemSelectedListener: OnItemSelectedListener?
        ) {
            textViewItem.setCompoundDrawablesWithIntrinsicBounds(item.icon ?: 0, 0, if(item.isChecked) R.drawable.ic_check else 0 , 0)
            textViewItem.text = item.value
            textViewItem.setOnClickListener {
                onItemSelectedListener?.invoke(item, position)
            }
        }
    }

    class EmptyViewHolder(override val containerView: View, emptyText: String) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        init {
            textViewItem.text = emptyText
        }
    }

    companion object {
        private const val KEY_SEARCH_NOT_FOUND = "SheetSelectionAdapter:search_not_found"
    }
}