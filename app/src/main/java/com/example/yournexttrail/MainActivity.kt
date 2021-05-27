package com.example.yournexttrail

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.ItemTouchHelper
import com.amplifyframework.AmplifyException
import com.amplifyframework.api.ApiException
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthUserAttribute
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.AWSDataStorePlugin
import com.amplifyframework.datastore.generated.model.User
import com.amplifyframework.logging.AndroidLoggingPlugin
import com.amplifyframework.logging.LogLevel
import com.amplifyframework.predictions.aws.AWSPredictionsPlugin
import com.google.android.material.navigation.NavigationView
import com.google.android.material.progressindicator.CircularProgressIndicator


class MainActivity : AppCompatActivity() {

    var value:String?= ""
    lateinit var spinner : CircularProgressIndicator
    lateinit var button : AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        val colorDrawable = ColorDrawable(Color.parseColor("#FF018786"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        spinner=findViewById(R.id.spinner)
        spinner.visibility=View.GONE
        button=findViewById(R.id.button2)
        button.visibility=View.VISIBLE

        try {
            // Add this line, to include the Auth plugin.

            Amplify.addPlugin(AWSDataStorePlugin())
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSPredictionsPlugin())
            Amplify.addPlugin(AWSApiPlugin())
            Amplify.configure(applicationContext)
            Log.i("Yournexttrail", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("Yournexttrail", "Could not initialize Amplify", error)
        }
        Log.i("Hello","HELLO WORLD")
       // getattributes()
//        Amplify.Auth.signInWithWebUI(this,
//            { Log.i("AuthQuickStart", "Signin OK = $it")
//
//                val intent = Intent(this, HomePage::class.java )
//                startActivity(intent)
//            },
//            { Log.e("AuthQuickStart", "Signin failed", it) }
//        )

        Amplify.Auth.fetchAuthSession(
            { //Log.i("AmplifyQuickstart", "Auth session = ${it.isSignedIn}")

                if (it.isSignedIn) {
                    getattributes()
            }

            },
            { Log.e("AmplifyQuickstart", "Failed to fetch auth session") }
        )


    }

    fun onbuttonClick( view: View) {
        Amplify.Auth.signInWithWebUI(this,
            { //Log.i("AuthQuickStart", "Signin OK = $it")
               getattributes()
            },
            { Log.e("AuthQuickStart", "Signin failed", it) }
        )
//        val viewIntent = Intent(Intent.ACTION_VIEW, Uri.parse(" https://yournexttrail-dev.auth.us-east-1.amazoncognito.com/"))
       // startActivity(viewIntent);
    }
    fun getattributes(){
        val intent = Intent(this, HomePage::class.java)

        Amplify.Auth.fetchUserAttributes(
                { attributes: List<AuthUserAttribute?> ->
//                    Log.i(
//                            "AuthDemo",
//                            "User attributes = ${attributes.toString()}"
//                    )
                    for (attribute in attributes) {
                        if (attribute != null) {
                            //Log.i("user", "user= ${attribute.getKey().getKeyString()}")
                            if (attribute.key.keyString == "email") {
                                value = attribute.value

                                //Log.i("value", "email= ${value}")
                                PreferenceManager.getDefaultSharedPreferences(this).edit().putString("email", value).apply()
                                Amplify.API.query(
                                        ModelQuery.list(User::class.java, User.EMAIL.contains(value)),
                                        { response ->  Log.i("Queryresponseforemail", response.data.items.count().toString())
                                            if(response.data.items.count() > 0) {
                                                response.data.forEach()
                                                {
                                                   // if (it.email == value)
                                                        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("userid", it.id).apply()
                                                      startActivity(intent)
                                                }
                                            }
                                            else {
                                                val user = User.Builder()
                                                        .email(value)
                                                        .build()
                                                Amplify.API.mutate(ModelMutation.create(user),{
                                                    PreferenceManager.getDefaultSharedPreferences(this).edit().putString("userid", it.data.id).apply()
                                                    startActivity(intent)
                                                }){ error: ApiException? ->
                                                    Log.e(
                                                            "MyAmplifyApp",
                                                            "Create failed",
                                                            error
                                                    )
                                                }

                                            }
//                                            runOnUiThread() {
//                                                val intent = Intent(this, HomePage::class.java)
//                                                startActivity(intent)
                                          //  }
                                        }, { Log.e("MyAmplifyApp", "Query failure", it) })
                            }
                        }
                    }
                }){ error: AuthException? ->
            Log.e(
                    "AuthDemo",
                    "Failed to fetch user attributes.",
                    error
            )
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AWSCognitoAuthPlugin.WEB_UI_SIGN_IN_ACTIVITY_CODE) {
            Amplify.Auth.handleWebUISignInResponse(data)
            button.visibility=View.GONE
            spinner.visibility=View.VISIBLE


        }
    }





}