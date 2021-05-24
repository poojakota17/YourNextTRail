package com.example.yournexttrail

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amplifyframework.api.ApiException
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.Reviews
import com.amplifyframework.datastore.generated.model.User
import com.google.android.material.bottomnavigation.BottomNavigationView


class MyReviews : AppCompatActivity() {
    lateinit var reviewlist : ArrayList<Reviews>
   lateinit var reviewlistadapter : ArrayAdapter<String>
    lateinit var  mAdaptter: MyReviewAdapter
    lateinit var myRecyclerView : RecyclerView
    lateinit var listview : ListView
    var updated: Boolean = false
    lateinit var bottomNavigation :BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_reviews)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        val colorDrawable = ColorDrawable(Color.parseColor("#FF018786"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        bottomNavigation= findViewById(R.id.bottom_navigation)
        bottomNavigation.setSelectedItemId(R.id.item2)
//        val homelayout : RelativeLayout =findViewById(R.id.myreview)
//        homelayout.setBackgroundColor(Color.parseColor("#C9A959"))
        val menu=bottomNavigation.menu
        val menu1= menu.findItem(R.id.item1)
        val menu2=menu.findItem(R.id.item2)
        val menu3=menu.findItem(R.id.item3)
        val menu4=menu.findItem(R.id.item4)
        val intent1= Intent(this,HomePage::class.java)
        val intent2= Intent(this,MyReviews::class.java)
        val intent3= Intent(this,MyRecommendations::class.java)
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
       // listview= findViewById(R.id.reviewlist)
        myRecyclerView = findViewById(R.id.myReviewRecycler)
        myRecyclerView.layoutManager= LinearLayoutManager(this)
        mAdaptter= MyReviewAdapter()
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
                      //  reviewlist.add(it.review + " : " + it.trail.title)
                        reviewlist.add(it)
                        //reviewlistadapter= ArrayAdapter(this,R.layout.reviewlisttext,reviewlist)
                            mAdaptter.updateitem(reviewlist)
                        runOnUiThread(){
                            myRecyclerView.adapter = mAdaptter
                        }
                       //listview.adapter=reviewlistadapter
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