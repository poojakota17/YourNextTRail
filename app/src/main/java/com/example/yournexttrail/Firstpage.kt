package com.example.yournexttrail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import android.widget.Toast.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthUserAttribute
import com.amplifyframework.core.Amplify
import com.google.android.material.navigation.NavigationView
import android.view.MenuItem
import android.widget.TextView


class Firstpage : AppCompatActivity() {
     var value:String?= ""

    lateinit var toggle : ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
//                             val headerview=findViewById<NavigationView>(R.id.navigationView).getHeaderView(0)
//                             val usertext=headerview.findViewById<TextView>(R.id.useremail)
//                            usertext.setText("Hello"+"\n"+attribute.value)

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

        setContentView(R.layout.activity_firstpage)

//        setSupportActionBar(findViewById(R.id.toolbar))
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
        val headerview=findViewById<NavigationView>(R.id.navigationView).getHeaderView(0)
                          val usertext=headerview.findViewById<TextView>(R.id.useremail)
                          usertext.setText("Hello"+"\n"+value)
        navview.setNavigationItemSelectedListener {
            when(it.itemId) {
                menu1.itemId -> Toast.makeText(applicationContext, menu1.title, LENGTH_SHORT).show()
                menu2.itemId -> Toast.makeText(applicationContext, menu2.title, LENGTH_SHORT).show()
                menu3.itemId -> Toast.makeText(applicationContext, menu3.title, LENGTH_SHORT).show()
                menu4.itemId->Amplify.Auth.signOut(
                    { Log.i("AuthQuickstart", "Signed out successfully")
//                        val intent=Intent(this,MainActivity::class.java)
//                        startActivity(intent)
                    },
                    { Log.e("AuthQuickstart", "Sign out failed", it) }

                )

            }
            true
    }
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

