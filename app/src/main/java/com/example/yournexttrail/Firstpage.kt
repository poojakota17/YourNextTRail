package com.example.yournexttrail

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import android.widget.Toast.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.amplifyframework.auth.*
import com.amplifyframework.auth.AuthUserAttribute
import com.amplifyframework.core.Amplify
import com.google.android.material.navigation.NavigationView
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.datastore.generated.model.User
import kotlinx.coroutines.runBlocking


public open class Firstpage : AppCompatActivity() {
     var value:String?= ""
   // protected var mdrawer : DrawerLayout = findViewById(R.id.drawer_layout)
 //rotected  var frameLayout : FrameLayout  = findViewById(R.id.content_frame)
    lateinit var toggle : ActionBarDrawerToggle
   override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firstpage)

        Amplify.Auth.fetchUserAttributes(
            { attributes: List<AuthUserAttribute?> -> Log.i(
                "AuthDemo",
                "User attributes = ${attributes.toString()}"
            )
                for(attribute in attributes){
                    if (attribute != null) {
                        Log.i("user","user= ${attribute.getKey().getKeyString()}")
                        if(attribute.key.keyString == "email"){
                            value=attribute.value

                            Log.i("value","email= ${value}")
                            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("email", value).apply()
                            Amplify.API.query(
                                ModelQuery.list(User::class.java, User.EMAIL.contains(value)),
                                { response ->
                                    response.data.forEach { todo ->
                                        Log.i("MyAmplifyApp", todo.id)
                                        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("userid", todo.id).apply()
                                    }
                                },
                                { Log.e("MyAmplifyApp", "Query failure", it) }
                            )
                            runOnUiThread()
                            {
                                val headerview = findViewById<NavigationView>(R.id.navigationView).getHeaderView(0)
                                val usertext = headerview.findViewById<TextView>(R.id.useremail)
                                usertext.setText("Hello" + "\n" + value)
                            }
                        }
                    }
                }

            }
        ) { error: AuthException? ->
            Log.e(
                "AuthDemo",
                "Failed to fetch user attributes.",
                error
            )
        }



      //setSupportActionBar(findViewById(R.id.toolbar))
        toggle= ActionBarDrawerToggle(this,findViewById(R.id.drawer_layout),R.string.open,R.string.close)
        findViewById<DrawerLayout>(R.id.drawer_layout).addDrawerListener(toggle)
        toggle.syncState()
       // toggle.isDrawerIndicatorEnabled = true
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val navview=findViewById<NavigationView>(R.id.navigationView)
        val menu=navview.menu
        val menu1= menu.findItem(R.id.item1)
        val menu2=menu.findItem(R.id.item2)
        val menu3=menu.findItem(R.id.item3)
        val menu4=menu.findItem(R.id.item4)
//        runOnUiThread()
//        {
//            val headerview = findViewById<NavigationView>(R.id.navigationView).getHeaderView(0)
//            val usertext = headerview.findViewById<TextView>(R.id.useremail)
//            usertext.setText("Hello" + "\n" + value)
//        }
        val intent1=Intent(this,HomePage::class.java)
        val intent2=Intent(this,MyReviews::class.java)
        val intent3=Intent(this,MyRecommendations::class.java)
        navview.setNavigationItemSelectedListener {
            when(it.itemId) {
                menu1.itemId -> startActivity(intent1)
                menu2.itemId -> startActivity(intent2)
                menu3.itemId -> startActivity(intent3)
                menu4.itemId->Amplify.Auth.signOut(
                    { Log.i("AuthQuickstart", "Signed out successfully")
                        Thread.sleep(500)
                        pushtomainactivity()

//                        val intent = Intent(this, MainActivity::class.java)
//                        startActivity(intent)

                    },
                    { Log.e("AuthQuickstart", "Sign out failed", it) }

                )

            }
            true
    }
        }

//    override fun setContentView(layoutResID: Int) {
//        //super.setContentView(layoutResID)
//        val fullview : DrawerLayout =
//            LayoutInflater.from(parent).inflate(R.layout.activity_firstpage,null) as DrawerLayout
//        val frame : FrameLayout = findViewById(R.id.content_frame)
//        LayoutInflater.from(parent).inflate(layoutResID, frame,true)
//        super.setContentView(fullview)
//       // setSupportActionBar(to)
//
//    }
fun pushtomainactivity(){
    val intent = Intent(this, MainActivity::class.java)
    startActivity(intent)
}


     override fun onOptionsItemSelected(item: MenuItem ): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

        fun onbuttonClick( view:View) {
            Toast.makeText( this,value, LENGTH_LONG).show()

        }
    }

