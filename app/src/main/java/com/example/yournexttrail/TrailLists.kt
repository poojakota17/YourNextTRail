package com.example.yournexttrail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.Consumer
import com.amplifyframework.datastore.generated.model.Trail
import com.google.android.play.core.tasks.OnSuccessListener

class TrailLists : AppCompatActivity(), TrailListAdaptter.Trailitemclicked {
    lateinit var  mAdaptter: TrailListAdaptter
    var updated: Boolean = false
    lateinit var myRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trail_lists)
        myRecyclerView = findViewById(R.id.myRecyclerView)

        myRecyclerView.layoutManager= LinearLayoutManager(this)
   //   val mAdaptter:TrailListAdaptter = TrailListAdaptter()
        mAdaptter=TrailListAdaptter(this)
        FetchData()
        while (!updated){
            Thread.sleep(500)
        }
        //myRecyclerView.adapter=mAdaptter
        updated=false
    }
    fun FetchData()
    {
        val result = ArrayList<Trail>()
//        val todo=Trail.builder()
//            .title("Vernal falls trail")
//            .description("njhvhgcfdxsd")
//            .level("hard")
//            .image("hgftrdrezew")
//            .build()
//        result.add(todo)
        mAdaptter.updateitem(result)

        Amplify.API.query(
            ModelQuery.list(Trail::class.java),
            { trails ->
                trails.data.forEach() {
                   result.add(it)
                    Log.i("MyAmplifyApp", "Title: ${it.title}")
                }
               mAdaptter.updateitem(result)
                myRecyclerView.adapter=mAdaptter
                updated = true
            },
            { Log.e("MyAmplifyApp", "Query failed",it ) }
        )

    }

    override fun onItemClicked(item: Trail) {
       // super.onItemClicked(item)
        val intent= Intent(this,MainActivity2::class.java)
        intent.putExtra("title",item.title)
        intent.putExtra("description",item.description)
        intent.putExtra("level",item.level)
        intent.putExtra("image",item.image)
        intent.putExtra("id",item.id)
        startActivity(intent)
    }

}