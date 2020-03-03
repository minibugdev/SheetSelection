package com.minibugdev.sheetselection.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.minibugdev.sheetselection.SheetSelection
import com.minibugdev.sheetselection.SheetSelectionAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonDefaultSheetSelection.setOnClickListener {
            val items = listOf(
                SheetSelectionAdapter.Item("1", "Default #1", null),
                SheetSelectionAdapter.Item("2", "Default #2", null),
                SheetSelectionAdapter.Item("3", "Default #3", null),
                SheetSelectionAdapter.Item("4", "Default #4", null)
            )

            SheetSelection.Builder(this)
                .title("Default Sheet Selection")
                .items(items)
                .onItemClickListener { item, position ->
                    textview.text = "You selected `${item.value}`, At position [$position]."
                }
                .show()
        }

        buttonCustomSheetSelection.setOnClickListener {
            SheetSelection.Builder(this)
                .title("Custom Sheet Selection")
                .items(
                    source = (0 until 99).map { "@$it" },
                    mapper = {
                        SheetSelectionAdapter.Item(
                            key = "key_$it",
                            value = "Custom $it",
                            icon = R.drawable.ic_face
                        )
                    }
                )
                .selectedPosition(2)
                .showDraggedIndicator(true)
                .theme(R.style.Theme_Custom_SheetSelection)
                .onItemClickListener { item, position ->
                    textview.text = "You selected `${item.value}`, At position [$position]."
                }
                .show()
        }
    }
}
