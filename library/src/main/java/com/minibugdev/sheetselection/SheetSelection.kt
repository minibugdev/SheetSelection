package com.minibugdev.sheetselection

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_sheet_selection.*

class SheetSelection private constructor() : BottomSheetDialogFragment() {

    var onItemClickListener: OnItemSelectedListener? = null

    override fun getTheme(): Int {
        val outValue = TypedValue()
        return if (requireContext().theme.resolveAttribute(R.attr.sheetSelectionTheme, outValue, true)) {
            outValue.resourceId;
        } else {
            R.style.Theme_SheetSelection
        }
    }

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
            textViewTitle.text = args.getString(ARGS_TITLE)
            recyclerViewSelectionItems.adapter = SheetSelectionAdapter(
                items = args.getParcelableArrayList(ARGS_ITEMS) ?: emptyList(),
                selectedPosition = args.getInt(ARGS_SELECTED_POSITION, NO_SELECT),
                onItemSelectedListener = internalOnItemSelectedListener
            )
        }
    }

    private val internalOnItemSelectedListener: OnItemSelectedListener = { item, position ->
        dismiss()
        onItemClickListener?.invoke(item, position)
    }

    class Builder(context: Context) {
        private val manager: FragmentManager? = when (context) {
            is FragmentActivity -> context.supportFragmentManager
            is Fragment -> context.requireFragmentManager()
            else -> null
        }

        private var title: String? = null
        private var items: List<SheetSelectionAdapter.Item> = emptyList()
        private var selectedPosition: Int = NO_SELECT
        private var listener: OnItemSelectedListener? = null

        fun title(title: String) = apply {
            this.title = title
        }

        fun selectedPosition(position: Int) = apply {
            this.selectedPosition = position
        }

        fun items(items: List<SheetSelectionAdapter.Item>) = apply {
            this.items = items
        }

        fun <T> items(
            source: List<T>,
            mapper: (T) -> SheetSelectionAdapter.Item
        ) = items(source.map { item -> mapper.invoke(item) })

        fun onItemClickListener(listener: OnItemSelectedListener) = apply {
            this.listener = listener
        }

        fun build() = SheetSelection().apply {
            arguments = Bundle()
                .apply {
                    putString(ARGS_TITLE, title)
                    putParcelableArrayList(ARGS_ITEMS, ArrayList(items))
                    putInt(ARGS_SELECTED_POSITION, selectedPosition)
                }
            onItemClickListener = listener
        }

        fun show() {
            manager?.let {
                build().show(it, "SheetSelection:TAG")
            }
        }
    }

    companion object {
        const val NO_SELECT = -1

        private const val ARGS_TITLE = "SheetSelection:ARGS_TITLE"
        private const val ARGS_ITEMS = "SheetSelection:ARGS_ITEMS"
        private const val ARGS_SELECTED_POSITION = "SheetSelection:ARGS_SELECTED_POSITION"
    }
}