package com.minibugdev.sheetselection

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_sheet_selection.*

typealias OnCompleteListener = (ArrayList<SheetSelectionItem>) -> Unit

class SheetSelection private constructor() : BottomSheetDialogFragment() {
    private var onMultipleItemClickListener: OnCompleteListener? = null
    private var previousList: List<SheetSelectionItem> = emptyList()
    private var selectedItemsList : ArrayList<SheetSelectionItem> = ArrayList()
    private val maxItemsChooseCount: Int by lazy {
        arguments?.getInt(ARGS_MAX_ITEM_CHOOSE_COUNT) ?: Int.MAX_VALUE
    }
    private val maxItemsChooseCountMsg: String? by lazy {
        arguments?.getString(ARGS_MAX_ITEM_CHOOSE_MESSAGE)
    }


    private val adapter by lazy {
         previousList = arguments?.getParcelableArrayList(ARGS_ITEMS) ?: emptyList()
        for (sheetSelectionItem in previousList) {
            if(sheetSelectionItem.isChecked)
                selectedItemsList.add(sheetSelectionItem)
        }
        SheetSelectionAdapter(
            source = previousList,
            searchNotFoundText = arguments?.getString(ARGS_SEARCH_NOT_FOUND_TEXT) ?: "Search not found.",
            onItemSelectedListener = onItemSelectedListener
        )
    }

    private val screenHeight by lazy {
        val statusBarHeight = try {
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            resources.getDimensionPixelSize(resourceId)
        } catch (e: Exception) {
            0
        }
        resources.displayMetrics.heightPixels - statusBarHeight
    }

    override fun getTheme(): Int = arguments?.getInt(ARGS_THEME) ?: super.getTheme()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_sheet_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { args ->
            if (args.getBoolean(ARGS_SHOW_DRAGGED_INDICATOR)) {
                draggedIndicator.visibility = View.VISIBLE
            }

            val title = args.getString(ARGS_TITLE)
            if (title.isNullOrEmpty()) {
                textViewTitle.visibility = View.GONE
                textViewTitle.text = null
            } else {
                textViewTitle.visibility = View.VISIBLE
                textViewTitle.text = title
            }

            if (args.getBoolean(ARGS_SEARCH_ENABLED)) {
                buttonSearch.visibility = View.VISIBLE
                buttonSearch.setOnClickListener(onSearchClickListener)
                searchView.setOnCloseListener(onSearchCloseListener)
                searchView.setOnQueryTextListener(onSearchQueryTextListener)
            }

            recyclerViewSelectionItems.setHasFixedSize(true)
            recyclerViewSelectionItems.adapter = adapter
        }

        doneButton.setOnClickListener{
            dismiss()
            onMultipleItemClickListener?.invoke(selectedItemsList)
        }
    }

    private fun updateSheetHeight(viewHeight: Int) {
        rootLayout.layoutParams = rootLayout.layoutParams
            .apply { height = viewHeight }
    }

    private val onItemSelectedListener: OnItemSelectedListener = { item, position ->
        if(arguments?.getBoolean(ARGS_FOR_MULTIPLE_SELECTION, false) == true){
            if(previousList.isNotEmpty()){
                val clickedItemInList = previousList.first { listItem -> listItem == item }
                if(selectedItemsList.contains(clickedItemInList)){
                    selectedItemsList.remove(clickedItemInList)
                    clickedItemInList.isChecked = false
                    adapter.notifyItemChanged(position)
                } else {
                    if(selectedItemsList.size < maxItemsChooseCount){
                        selectedItemsList.add(clickedItemInList)
                        clickedItemInList.isChecked = true
                        adapter.notifyItemChanged(position)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            if(maxItemsChooseCountMsg.isNullOrEmpty()) "Cannot choose more than $maxItemsChooseCount items" else maxItemsChooseCountMsg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else {
            dismiss()
            selectedItemsList.clear()
            selectedItemsList.add(item)
            onMultipleItemClickListener?.invoke(selectedItemsList)
        }
    }

    private val onSearchClickListener = View.OnClickListener {
        (dialog as? BottomSheetDialog)?.run {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        updateSheetHeight(screenHeight)
        viewSwitcherHeader.displayedChild = 1
        searchView.isIconified = false
    }

    private val onSearchCloseListener = SearchView.OnCloseListener {
        updateSheetHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
        viewSwitcherHeader.displayedChild = 0
        true
    }

    private val onSearchQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(newText: String?): Boolean {
            adapter.search(newText)
            return true
        }

        override fun onQueryTextSubmit(query: String?): Boolean {
            adapter.search(query)
            return true
        }
    }

    class Builder(private val context: Context) {
        private val manager: FragmentManager? = when (context) {
            is FragmentActivity -> context.supportFragmentManager
            is Fragment -> context.requireFragmentManager()
            else -> null
        }

        @StyleRes
        private var themeId: Int = R.style.Theme_SheetSelection
        private var title: String? = null
        private var items: List<SheetSelectionItem> = emptyList()
        private var showDraggedIndicator: Boolean = false
        private var searchEnabled: Boolean = false
        private var forMultipleSelection: Boolean = false
        private var maxItemChooseCount: Int = 0
        private var maxItemChooseMessage: String? = null
        private var searchNotFoundText: String? = null
        private var listener: OnCompleteListener? = null

        fun theme(@StyleRes themeId: Int) = apply {
            this.themeId = themeId
        }

        fun title(title: String?) = apply {
            this.title = title
        }

        fun items(items: List<SheetSelectionItem>) = apply {
            this.items = items
        }

        fun <T> items(
            source: List<T>,
            mapper: (T) -> SheetSelectionItem
        ) = items(source.map { item -> mapper.invoke(item) })

        fun showDraggedIndicator(show: Boolean) = apply {
            this.showDraggedIndicator = show
        }

        fun searchEnabled(enabled: Boolean) = apply {
            this.searchEnabled = enabled
        }

        fun searchNotFoundText(text: String) = apply {
            this.searchNotFoundText = text
        }

        fun forMultipleSelection(enabled: Boolean) = apply {
            this.forMultipleSelection = enabled
        }

        fun maxItemChooseCount(count: Int, message : String? = null) = apply {
            this.maxItemChooseCount = count
            this.maxItemChooseMessage = message
        }

        fun searchNotFoundText(@StringRes textResId: Int) = apply {
            this.searchNotFoundText = context.getString(textResId)
        }

        fun onCompleteListener(listener: OnCompleteListener) = apply {
            this.listener = listener
        }

        fun build() = SheetSelection().apply {
            arguments = Bundle()
                .apply {
                    putInt(ARGS_THEME, themeId)
                    putInt(ARGS_MAX_ITEM_CHOOSE_COUNT, maxItemChooseCount)
                    putString(ARGS_MAX_ITEM_CHOOSE_MESSAGE, maxItemChooseMessage)
                    putString(ARGS_TITLE, title)
                    putParcelableArrayList(ARGS_ITEMS, ArrayList(items))
                    putBoolean(ARGS_SHOW_DRAGGED_INDICATOR, showDraggedIndicator)
                    putBoolean(ARGS_SEARCH_ENABLED, searchEnabled)
                    putBoolean(ARGS_FOR_MULTIPLE_SELECTION, forMultipleSelection)
                    putString(ARGS_SEARCH_NOT_FOUND_TEXT, searchNotFoundText)
                }
            onMultipleItemClickListener = listener
        }

        fun show() {
            manager?.let {
                build().show(it, "SheetSelection:TAG")
            }
        }
    }

    companion object {
        private const val TAG = "SheetSelection"
        const val NO_SELECT = -1
        private const val ARGS_THEME = "SheetSelection:ARGS_THEME"
        private const val ARGS_TITLE = "SheetSelection:ARGS_TITLE"
        private const val ARGS_ITEMS = "SheetSelection:ARGS_ITEMS"
        private const val ARGS_SEARCH_NOT_FOUND_TEXT = "SheetSelection:ARGS_SEARCH_NOT_FOUND_TEXT"
        private const val ARGS_SHOW_DRAGGED_INDICATOR = "SheetSelection:ARGS_SHOW_DRAGGED_INDICATOR"
        private const val ARGS_SEARCH_ENABLED = "SheetSelection:ARGS_SEARCH_ENABLED"
        private const val ARGS_FOR_MULTIPLE_SELECTION = "SheetSelection:ARGS_FOR_MULTIPLE_SELECTION"
        private const val ARGS_MAX_ITEM_CHOOSE_COUNT = "SheetSelection:ARGS_MAX_ITEM_CHOOSE_COUNT"
        private const val ARGS_MAX_ITEM_CHOOSE_MESSAGE = "SheetSelection:ARGS_MAX_ITEM_CHOOSE_MESSAGE"
    }
}