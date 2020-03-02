package com.minibugdev.bottomsheetselection.demo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.minibugdev.bottomsheetselection.SheetSelection
import com.minibugdev.bottomsheetselection.SheetSelectionAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SheetSelectionAdapter.OnItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonShowSheetSelection.setOnClickListener {

            val items = listOf(
                SheetSelectionAdapter.Item("1", "Item #1", null),
                SheetSelectionAdapter.Item("2", "Item #2", null),
                SheetSelectionAdapter.Item("3", "Item #3", null),
                SheetSelectionAdapter.Item("4", "Item #4", null)
            )

            SheetSelection.Builder(this)
                .title("This is Title from Activity")
                .items(items)
                .selectedPosition(0)
                .show()
        }

        buttonFragment.setOnClickListener {
            startActivity(Intent(this, FragmentActivity::class.java))
        }
    }

    override fun onSelected(item: SheetSelectionAdapter.Item, position: Int) {
        Toast.makeText(this, item.value, Toast.LENGTH_SHORT)
            .show()
    }
}
