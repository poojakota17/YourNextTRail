package com.example.yournexttrail

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amplifyframework.api.ApiException
import com.amplifyframework.api.graphql.GraphQLResponse
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.Attribute
import com.amplifyframework.datastore.generated.model.Trail
import com.amplifyframework.datastore.generated.model.TrailAttribute
import com.amplifyframework.datastore.generated.model.UserAttribute
import kotlinx.coroutines.runBlocking


class MyRecommendations : AppCompatActivity(), Recommendationadapter.Trailitemclicked {
    lateinit var  mAdapter: Recommendationadapter
    lateinit var  myRecyclerView: RecyclerView
    lateinit var result: ArrayList<Trail>
    var totalSync = 0
    var currentSync = 0
    var updated: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_recommendations)
        myRecyclerView = findViewById(R.id.myRecyclerView1)
        myRecyclerView.layoutManager= LinearLayoutManager(this)
        mAdapter= Recommendationadapter(this)
        updated=false
        getmyrecommendedtrails()
        while (currentSync < 1 || currentSync<totalSync){
            Thread.sleep(500)
        }
        Log.i("resultSize", result.size.toString())
        mAdapter.updateitem(result)
        myRecyclerView.adapter = mAdapter
        updated=false
    }

    fun getmyrecommendedtrails()
    {val e= PreferenceManager.getDefaultSharedPreferences(this).getString("email", "default value")

        Amplify.API.query(
            ModelQuery.list(UserAttribute::class.java, UserAttribute.EMAIL.contains(e)),
            { response ->
                result = ArrayList()
                totalSync = response.data.count()
                Log.i("totalSync", totalSync.toString())
                currentSync = 0
                response.data.forEach() { todo ->
                    gettrailattributes(todo.name)

                    //Thread.sleep(2_000)
                    Log.i("todoname", todo.name)
                }
                updated=true

            }
        ) { error: ApiException? ->
            Log.e(
                "MyAmplifyApp",
                "Query failure",
                error
            )
        }
    }
    fun gettrailattributes(str: String){
        Amplify.API.query(
            ModelQuery.list(
                Attribute::class.java,
                Attribute.NAME.contains(str)
            ),
            { response ->
                for (todo in response.data) {
                    Log.i("todoid", todo.id)
                    todo.trails.forEach(){
                        Log.i("trailtitle",it.trail.title)
                        result.add(it.trail)
                    }
                }
                currentSync++
                Log.i("currentSync", currentSync.toString())
            }
        ) { error: ApiException? ->
            Log.e(
                "MyAmplifyApp",
                "Query failure",
                error
            )
        }

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