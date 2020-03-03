package com.minibugdev.sheetselection.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.minibugdev.sheetselection.SheetSelection
import com.minibugdev.sheetselection.SheetSelectionItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonDefaultSheetSelection.setOnClickListener {
            val items = listOf(
                SheetSelectionItem("1", "Item #1", R.drawable.ic_extension),
                SheetSelectionItem("2", "Item #2", R.drawable.ic_nature),
                SheetSelectionItem("3", "Item #3", R.drawable.ic_fingerprint),
                SheetSelectionItem("4", "Item #4", R.drawable.ic_face)
            )

            SheetSelection.Builder(this)
                .title("Sheet Selection")
                .items(items)
                .selectedPosition(2)
                .showDraggedIndicator(true)
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
                        SheetSelectionItem(
                            key = "key_$it",
                            value = "Custom $it",
                            icon = R.drawable.ic_motorcycle
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
