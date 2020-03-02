package com.minibugdev.bottomsheetselection

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SheetSelectionAdapter.OnItemSelectedListener {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		button.setOnClickListener {

			val items = listOf(
					SheetSelectionAdapter.Item("1", "Item #1", null),
					SheetSelectionAdapter.Item("2", "Item #2", null),
					SheetSelectionAdapter.Item("3", "Item #3", null),
					SheetSelectionAdapter.Item("4", "Item #4", null)
			)

			SheetSelection.Builder(this)
				.title("This is Title")
				.items(items)
				.selectedPosition(0)
				.show()
		}
	}

	override fun onSelected(item: SheetSelectionAdapter.Item, position: Int) {
		Toast.makeText(this, item.value, Toast.LENGTH_SHORT)
			.show()
	}
}
