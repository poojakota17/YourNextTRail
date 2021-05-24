package com.example.yournexttrail

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amplifyframework.datastore.generated.model.Trail
import com.bumptech.glide.Glide

class Recommendationadapter (private  val listener: Trailitemclicked): RecyclerView.Adapter<Recommendationadapter.ViewHolder>() {
    private val items: ArrayList<Trail> = ArrayList()
//    private val parklists = arrayOf(
//        "#1 - Death Valley National Park",
//        "#2 - Yosemite National Park",
//        "#3 - Sequoia and Kings Canyon National Park",
//        "#4 - Redwood National and State Park",
//        "#5 - Joshua Tree National Park",
//        "#6 - Lassen Volcanic National Park",
//        "#7 - Pinnacles National Park",
//        "#8 - Point Reyes National Seashore"
//    )

//    private val reviewlist =
//        arrayOf("4.9/5", "4.8/5", "4.8/5", "4.8/5", "4.8/5", "4.7/5", "4.7/5", "4.5/5")

//    private val imageslist= intArrayOf(R.drawable.deathvalley, R.drawable.yosemite,R.drawable.sequoia,R.drawable.redwood,
//        R.drawable.joshua,R.drawable.lassen, R.drawable.pinnacles, R.drawable.point_reyes )


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        var title: TextView
        var review: TextView
        var share: ImageButton

        init {
            image = itemView.findViewById(R.id.park_image)
            title = itemView.findViewById(R.id.parkname)
            review = itemView.findViewById(R.id.park_review)
            share = itemView.findViewById(R.id.sharebutton)
        }
    }

    //    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//      holder.title.text= parklists[position]
//        holder.review.text=  reviewlist[position]
//        holder.image.setImageResource(imageslist[position])
//       // holder.itemView.setOnClickListener { v: View ->  }
//    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val oneview: View =
            LayoutInflater.from(parent.context).inflate(R.layout.trailcardview, parent, false)
        val viewholder = ViewHolder(oneview)
        oneview.setOnClickListener {
            listener.onItemClicked(items[viewholder.adapterPosition])
        }
        return viewholder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentitem = items[position]
        holder.title.text = currentitem.title
        holder.review.text = currentitem.level
        Glide.with(holder.itemView.context).load(currentitem.image).into(holder.image)
        holder.share.setOnClickListener(View.OnClickListener {
            val intent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TITLE, currentitem.description)
                putExtra(
                    Intent.EXTRA_TEXT, "Let's go for a trip to "
                            + currentitem.title +"\n" +currentitem.image
                )
                type ="text/plain"
            }
            val shareIntent = Intent.createChooser(intent, null)
            it.context.startActivity(shareIntent)


        })
        //holder.image.setImageResource(imageslist[position])
    }

    fun updateitem(updateitem: ArrayList<Trail>) {
        items.clear()
        items.addAll(updateitem)
        notifyDataSetChanged()
    }

    interface Trailitemclicked {
        fun onItemClicked(item: Trail) {

        }
    }
}
