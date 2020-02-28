package com.minibugdev.bottomsheetselection

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_sheet_selection.*

class SheetSelection : BottomSheetDialogFragment() {

	private var onItemClickListener: SheetSelectionAdapter.OnItemSelectedListener? = null

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.dialog_sheet_selection, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		arguments?.let { args ->
			textViewTitle.text = args.getString(ARGS_TITLE)
			recyclerViewSelectionItems.adapter = SheetSelectionAdapter(
					items = args.getParcelableArrayList(ARGS_ITEMS) ?: emptyList(),
					selectedPosition = args.getInt(ARGS_SELECTED_POSITION, NO_SELECT),
					onItemSelectedListener = internalOnItemClickListener
			)
		}
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		onItemClickListener = (context as? SheetSelectionAdapter.OnItemSelectedListener)
	}

	private val internalOnItemClickListener = object : SheetSelectionAdapter.OnItemSelectedListener {
		override fun onSelected(item: SheetSelectionAdapter.Item, position: Int) {
			dismiss()
			onItemClickListener?.onSelected(item, position)
		}
	}

	companion object {
		const val NO_SELECT = -1

		private const val ARGS_TITLE = "SheetSelection:ARGS_TITLE"
		private const val ARGS_ITEMS = "SheetSelection:ARGS_ITEMS"
		private const val ARGS_SELECTED_POSITION = "SheetSelection:ARGS_SELECTED_POSITION"

		fun getInstance(title: String,
		                items: List<SheetSelectionAdapter.Item>,
		                selectedPosition: Int) = SheetSelection()
			.apply {
				arguments = Bundle().apply {
					putString(ARGS_TITLE, title)
					putParcelableArrayList(ARGS_ITEMS, ArrayList(items))
					putInt(ARGS_SELECTED_POSITION, selectedPosition)
				}
			}
	}
}