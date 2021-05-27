package com.example.yournexttrail

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amplifyframework.api.ApiException
import com.amplifyframework.api.graphql.GraphQLResponse
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.model.Model
import com.amplifyframework.core.model.query.Where
import com.amplifyframework.datastore.generated.model.Reviews

class MyReviewAdapter() : RecyclerView.Adapter<MyReviewAdapter.ViewHolder>() {
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
//        Log.i("d1", currentitem.user.email)
//        Log.i("d2", currentitem.review)
        holder.title.text = currentitem.trail.title
        holder.review.text = currentitem.review
        if (currentitem.senitment == "POSITIVE") {

            //  Glide.with(holder.itemView.context).load(get).into(holder.image)
            holder.image.setImageResource(R.drawable.ic_baseline_sentiment_satisfied_alt_24)
        }
        else if(currentitem.senitment == "NEGATIVE" ){
            //  Glide.with(holder.itemView.context).load(get).into(holder.image)
            holder.image.setImageResource(R.drawable.ic_baseline_sentiment_very_dissatisfied_24)

        }


    }

    fun updateitem(updateitem: ArrayList<Reviews>) {
        items.clear()
        items.addAll(updateitem)
        notifyDataSetChanged()
    }
    fun deleteitem(pos : Int){

        items.removeAt(pos)
        deletereview(pos)
        notifyItemRemoved(pos)


    }
    fun deletereview(pos : Int){
        Amplify.DataStore.delete(items[pos],{
            Log.i("deleted review",it.toString())
        }){
            Log.e("MyAmplifyApp", "Delete failed", it)
        }
    }

}