package com.minibugdev.sheetselection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_sheet_selection.*

class SheetSelection private constructor() : BottomSheetDialogFragment() {

    private var onItemClickListener: SheetSelectionAdapter.OnItemSelectedListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_sheet_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerListener()

        arguments?.let { args ->
            textViewTitle.text = args.getString(ARGS_TITLE)
            recyclerViewSelectionItems.adapter =
                SheetSelectionAdapter(
                    items = args.getParcelableArrayList(ARGS_ITEMS) ?: emptyList(),
                    selectedPosition = args.getInt(
                        ARGS_SELECTED_POSITION,
                        NO_SELECT
                    ),
                    onItemSelectedListener = internalOnItemClickListener
                )
        }
    }

    private fun registerListener() {
        onItemClickListener = if (targetFragment != null) {
            targetFragment as? SheetSelectionAdapter.OnItemSelectedListener
        } else {
            activity as? SheetSelectionAdapter.OnItemSelectedListener
        }
    }

    private val internalOnItemClickListener =
        object : SheetSelectionAdapter.OnItemSelectedListener {
            override fun onSelected(item: SheetSelectionAdapter.Item, position: Int) {
                dismiss()
                onItemClickListener?.onSelected(item, position)
            }
        }

    class Builder {
        private val manager: FragmentManager
        private val target: Fragment?

        private var title: String? = null
        private var items: List<SheetSelectionAdapter.Item> = emptyList()
        private var selectedPosition: Int =
            NO_SELECT

        constructor(activity: FragmentActivity) {
            this.target = null
            this.manager = activity.supportFragmentManager
        }

        constructor(fragment: Fragment) {
            this.target = fragment
            this.manager = fragment.requireFragmentManager()
        }

        fun title(title: String) = apply { this.title = title }
        fun items(items: List<SheetSelectionAdapter.Item>) = apply { this.items = items }
        fun selectedPosition(selectedPosition: Int) =
            apply { this.selectedPosition = selectedPosition }

        fun build() = SheetSelection().apply {
            arguments = Bundle()
                .apply {
                    putString(ARGS_TITLE, title)
                    putParcelableArrayList(ARGS_ITEMS, ArrayList(items))
                    putInt(ARGS_SELECTED_POSITION, selectedPosition)
                }
            setTargetFragment(target, 99)
        }

        fun show() {
            build().show(manager, "SheetSelection:TAG")
        }
    }

    companion object {
        const val NO_SELECT = -1

        private const val ARGS_TITLE = "SheetSelection:ARGS_TITLE"
        private const val ARGS_ITEMS = "SheetSelection:ARGS_ITEMS"
        private const val ARGS_SELECTED_POSITION = "SheetSelection:ARGS_SELECTED_POSITION"
    }
}