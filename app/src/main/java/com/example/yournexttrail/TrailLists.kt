package com.example.yournexttrail

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.play.core.tasks.OnSuccessListener

class TrailLists : AppCompatActivity(), TrailListAdaptter.Trailitemclicked {
    lateinit var  mAdaptter: TrailListAdaptter
    var updated: Boolean = false
    lateinit var bottomNavigation :BottomNavigationView
    lateinit var myRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trail_lists)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        val colorDrawable = ColorDrawable(Color.parseColor("#FF018786"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        bottomNavigation= findViewById(R.id.bottom_navigation)
        bottomNavigation.setSelectedItemId(R.id.item1)
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