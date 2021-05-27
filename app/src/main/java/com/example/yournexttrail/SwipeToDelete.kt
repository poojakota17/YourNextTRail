package com.example.yournexttrail

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeToDelete (var adapter: MyReviewAdapter): ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        TODO("Not yet implemented")
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
         var pos = viewHolder.adapterPosition
        adapter.deleteitem(pos)

    }

}






