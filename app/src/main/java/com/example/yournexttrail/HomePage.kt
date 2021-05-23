package com.example.yournexttrail

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread
import com.amplifyframework.api.ApiException
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.UserAttribute
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.navigation.NavigationView


class HomePage : AppCompatActivity() {

    lateinit var bottomNavigation :BottomNavigationView
    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        val colorDrawable = ColorDrawable(Color.parseColor("#FF018786"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        //val view : View = LayoutInflater.from(this).inflate(R.layout.activity_home_page,frameLayout)
        setContentView(R.layout.activity_home_page)
//      val homelayout : RelativeLayout=findViewById(R.id.homelayout)
//        homelayout.setBackgroundColor(Color.parseColor("#C0CDDA"))
        getLikings()
       // val navview=findViewById<NavigationView>(R.id.navigationView)

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
                        pushtomainactivity()
                        overridePendingTransition(0,0)
                        true

//                        val intent = Intent(this, MainActivity::class.java)
//                        startActivity(intent)

                    },
                    { Log.e("AuthQuickstart", "Sign out failed", it) }

                )

            }
            false
        })



    }
    fun pushtomainactivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun getLikings(){
        val chipGroup : ChipGroup = findViewById(R.id.chipgroup)
        val e= PreferenceManager.getDefaultSharedPreferences(this).getString("email", "default value")

        Amplify.API.query(
            ModelQuery.list(UserAttribute::class.java, UserAttribute.EMAIL.contains(e)),
            { response ->
                for (todo in response.data) {
                    runOnUiThread {
                        // Stuff that updates the UI

                        val chip = Chip(this, null, R.style.Widget_MaterialComponents_Chip_Choice)
                            .apply { text = todo.name
                                chipIcon=getDrawable(R.drawable.ic_baseline_favorite_24)
                            }

                        chipGroup.addView(chip)

                        Log.i("MyAmplifyApp", todo.getName())
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

    fun onSearch(view: View) {
        val intent = Intent(this,TrailLists::class.java)
       startActivity(intent)
    }
}