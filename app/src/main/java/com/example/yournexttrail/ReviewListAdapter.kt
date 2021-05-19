package com.example.yournexttrail


import com.example.yournexttrail.R



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amplifyframework.datastore.generated.model.Reviews
import com.amplifyframework.datastore.generated.model.Trail
import com.bumptech.glide.Glide

class ReviewListAdapter : RecyclerView.Adapter<ReviewListAdapter.ViewHolder>() {
    private val items: ArrayList<Reviews> = ArrayList()


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var review: TextView

        init {


            title = itemView.findViewById(R.id.email)
            review = itemView.findViewById(R.id.reviewtextview)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val oneview: View =
            LayoutInflater.from(parent.context).inflate(R.layout.trailcardview, parent, false)
       // val viewholder = ViewHolder(oneview)

        return ViewHolder(oneview)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentitem = items[position]
       // if(currentitem)
        holder.title.text = currentitem.user.email
        holder.review.text = currentitem.review

    }

    fun updateitem(updateitem: ArrayList<Reviews>) {
        items.clear()
        items.addAll(updateitem)
        notifyDataSetChanged()
    }


}
