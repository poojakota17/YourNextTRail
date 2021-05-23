package com.example.yournexttrail

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.runBlocking


class MyRecommendations : AppCompatActivity(), Recommendationadapter.Trailitemclicked {
    lateinit var  mAdapter: Recommendationadapter
    lateinit var  myRecyclerView: RecyclerView
    lateinit var result: ArrayList<Trail>
    lateinit var bottomNavigation :BottomNavigationView

    var totalSync = 0
    var currentSync = 0
    var updated: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_recommendations)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        val colorDrawable = ColorDrawable(Color.parseColor("#FF018786"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        bottomNavigation= findViewById(R.id.bottom_navigation)
        bottomNavigation.setSelectedItemId(R.id.item3)
        val menu=bottomNavigation.menu
        val menu1= menu.findItem(R.id.item1)
        val menu2=menu.findItem(R.id.item2)
        val menu3=menu.findItem(R.id.item3)
        val menu4=menu.findItem(R.id.item4)
        val intent1=Intent(this,HomePage::class.java)
        val intent2=Intent(this,MyReviews::class.java)
        val intent3=Intent(this,MyRecommendations::class.java)
        bottomNavigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener {
            when(it.itemId){
                menu1.itemId -> {
                    startActivity(intent1)
                    overridePendingTransition(0,0)
                    true
                }
                menu2.itemId -> {
                    startActivity(intent2)
                    overridePendingTransition(0,0)
                    true
                }
                menu3.itemId -> {
                    startActivity(intent3)
                    overridePendingTransition(0,0)
                    true
                }
                menu4.itemId->Amplify.Auth.signOut(
                    { Log.i("AuthQuickstart", "Signed out successfully")
                        Thread.sleep(500)
                        //pushtomainactivity()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(0,0)
                        true



                    },
                    { Log.e("AuthQuickstart", "Sign out failed", it) }

                )

            }
            false
        })
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