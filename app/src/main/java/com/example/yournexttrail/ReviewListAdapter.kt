package com.example.yournexttrail


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amplifyframework.datastore.generated.model.Reviews

class ReviewListAdapter() : RecyclerView.Adapter<ReviewListAdapter.ViewHolder>() {
    private val items: ArrayList<Reviews> = ArrayList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var review: TextView

        init {
            title = itemView.findViewById(R.id.email)
            review = itemView.findViewById(R.id.reviewtext)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val oneview: View =
            LayoutInflater.from(parent.context).inflate(R.layout.reviewview, parent, false)
       // val viewholder = ViewHolder(oneview)

        return ViewHolder(oneview)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentitem = items[position]
       // if(currentitem)
        Log.i("d1", currentitem.user.email)
        Log.i("d2", currentitem.review)
        //if(currentitem.senitment=="POSITIVE")
        holder.title.text = currentitem.user.email
        holder.review.text = currentitem.review


    }

    fun updateitem(updateitem: ArrayList<Reviews>) {
        items.clear()
        items.addAll(updateitem)
        notifyDataSetChanged()
    }


}
