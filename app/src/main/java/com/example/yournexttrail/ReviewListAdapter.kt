package com.example.yournexttrail


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.amplifyframework.datastore.generated.model.Reviews
import com.bumptech.glide.Glide

class ReviewListAdapter() : RecyclerView.Adapter<ReviewListAdapter.ViewHolder>() {
    private val items: ArrayList<Reviews> = ArrayList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var review: TextView
        var image: ImageView

        init {
            title = itemView.findViewById(R.id.email)
            review = itemView.findViewById(R.id.reviewtext)
            image = itemView.findViewById(R.id.emoji)
        }
    }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val oneview: View =
                LayoutInflater.from(parent.context).inflate(R.layout.trailreviews, parent, false)
            // val viewholder = ViewHolder(oneview)

            return ViewHolder(oneview)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val currentitem = items[position]
            // if(currentitem)
//            Log.i("d1", currentitem.user.email)
//            Log.i("d2", currentitem.review)
            holder.title.text = currentitem.user.email.substringBefore("@")
            holder.review.text = currentitem.review
            if (currentitem.senitment == "POSITIVE") {
                //  Glide.with(holder.itemView.context).load(get).into(holder.image)
                holder.image.setImageResource(R.drawable.ic_baseline_sentiment_satisfied_alt_24)
            }
            else if(currentitem.senitment == "NEGATIVE" ){
                holder.image.setImageResource(R.drawable.ic_baseline_sentiment_very_dissatisfied_24)
            }


        }

        fun updateitem(updateitem: ArrayList<Reviews>) {
            items.clear()
            items.addAll(updateitem)
            notifyDataSetChanged()
        }

        fun additem(item: Reviews) {
            items.add(item)
            notifyDataSetChanged()
        }

    }


