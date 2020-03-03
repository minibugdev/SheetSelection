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

        buttonShowSheetSelection.setOnClickListener {

            val items = listOf("#1", "#2", "#3", "#4")
            SheetSelection.Builder(this)
                .title("This is Title from Activity")
                .items(
                    source = items,
                    mapper = { SheetSelectionAdapter.Item("key_$it", "Item $it", null) }
                )
                .selectedPosition(1)
                .onItemClickListener { item, position ->
                    textview.text = "You selected ${item.value}, At position $position."
                }
                .show()
        }
    }
}
