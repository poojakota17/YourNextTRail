package com.example.yournexttrail

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.amplifyframework.api.ApiException
import com.amplifyframework.api.graphql.GraphQLResponse
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.User
import kotlinx.coroutines.runBlocking


class MyReviews : AppCompatActivity() {
    lateinit var reviewlist : ArrayList<String>
   lateinit var reviewlistadapter : ArrayAdapter<String>
    lateinit var listview : ListView
    var updated: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_reviews)
        listview= findViewById(R.id.reviewlist)
        getmyreviews()
        while (!updated){
            Thread.sleep(1_000)
        }
        //myRecyclerView.adapter=mAdaptter
        updated=false
//        Thread.sleep(1_000)
    }

    fun getmyreviews(){
        reviewlist= ArrayList()
        val e= PreferenceManager.getDefaultSharedPreferences(this).getString("email", "default value")
        Amplify.API.query(
            ModelQuery.list(User::class.java, User.EMAIL.contains(e)),
            { response ->
                for (todo in response.data) {
                    todo.reviews.forEach(){
                        reviewlist.add(it.review + " : " + it.trail.title)
                        reviewlistadapter= ArrayAdapter(this,R.layout.reviewlisttext,reviewlist)

                       listview.adapter=reviewlistadapter
                        updated=true
                        Log.i("reviews", it.review)
                        Log.i("sentiment",it.senitment)

                        Log.i("trailname",it.trail.title)
                    }

                }
            }
        ) { error: ApiException? ->
            Log.e(
                "MyAmplifyApp",
                "Query failure",
                error
            )
        }
    }


}