package com.minibugdev.bottomsheetselection.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.minibugdev.bottomsheetselection.SheetSelection
import com.minibugdev.bottomsheetselection.SheetSelectionAdapter
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(), SheetSelectionAdapter.OnItemSelectedListener {

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonShowSheetSelection.setOnClickListener {

            val items = listOf(
                SheetSelectionAdapter.Item("1", "Item #1", null),
                SheetSelectionAdapter.Item("2", "Item #2", null),
                SheetSelectionAdapter.Item("3", "Item #3", null),
                SheetSelectionAdapter.Item("4", "Item #4", null)
            )

            SheetSelection.Builder(this)
                .title("This is Title from Fragment")
                .items(items)
                .selectedPosition(1)
                .show()
        }
    }

    override fun onSelected(item: SheetSelectionAdapter.Item, position: Int) {
        Toast.makeText(requireContext(), item.value, Toast.LENGTH_SHORT)
            .show()
    }
}
