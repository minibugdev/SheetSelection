package com.minibugdev.sheetselection.demo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.minibugdev.sheetselection.SheetSelection
import com.minibugdev.sheetselection.SheetSelectionItem
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonDefaultSheetSelection.setOnClickListener {
            val items = listOf(
                SheetSelectionItem("1", "Item #1", false, R.drawable.ic_extension),
                SheetSelectionItem("2", "Item #2", false, R.drawable.ic_nature),
                SheetSelectionItem("3", "Item #3", false, R.drawable.ic_fingerprint),
                SheetSelectionItem("4", "Item #4", false, R.drawable.ic_face),
                SheetSelectionItem("5", "Item #5", true, R.drawable.ic_extension),
                SheetSelectionItem("6", "Item #6", false, R.drawable.ic_fingerprint)
            )

            SheetSelection.Builder(this)
                .title("Sheet Selection")
                .items(items)
                //This will enable multiple Item choose feature
                .forMultipleSelection(true)
                //Maximum item choose limit
                .maxItemChooseCount(2)
                // Also you can use this
                // .maxItemChooseCount(2, Max Item reached)
                .showDraggedIndicator(true)
                .searchEnabled(true)
                .onCompleteListener {
                    if (it.isEmpty())
                        return@onCompleteListener

                    // Do what ever you want with these selected items.
                    Log.e(TAG, "OnCompleteSelection: $it")
                    for (sheetSelectionItem in it) {
                        Log.e(TAG, "OnCompleteSelection: ${sheetSelectionItem.value}")
                    }
                    textview.text = "You selected ${it[0].value}."
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
                            isChecked = Random().nextBoolean(),
                            icon = R.drawable.ic_motorcycle
                        )
                    }
                )
                //.selectedPosition(2)
                .showDraggedIndicator(true)
                .searchEnabled(true)
                .forMultipleSelection(true)
                .searchNotFoundText("Nothing!!")
                .theme(R.style.Theme_Custom_SheetSelection)
                .onCompleteListener {
                    if (it.isEmpty())
                        return@onCompleteListener

                    // Do what ever you want with these selected items.
                    for (sheetSelectionItem in it) {
                        Log.e(TAG, "OnCompleteSelection: $it")
                    }
                    textview.text = "You selected ${it[0].value}, At position"
                }
//                .onItemClickListener { item, position ->
//                    textview.text = "You selected `${item.value}`, At position [$position]."
//                }
                .show()
        }
    }
}
