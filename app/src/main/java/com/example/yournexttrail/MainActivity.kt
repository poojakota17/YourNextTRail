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
import com.amplifyframework.AmplifyException
import com.amplifyframework.api.aws.AWSApiPlugin
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


class MainActivity : AppCompatActivity() {

    var value:String?= ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        val colorDrawable = ColorDrawable(Color.parseColor("#FF018786"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)

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
//        Amplify.Auth.signInWithWebUI(this,
//            { Log.i("AuthQuickStart", "Signin OK = $it")
//
//
//            },
//            { Log.e("AuthQuickStart", "Signin failed", it) }
//        )

//        Amplify.Auth.fetchAuthSession(
//            { Log.i("AmplifyQuickstart", "Auth session = $it")
////                Thread.sleep(1_000)
////                val intent = Intent(this, Firstpage::class.java )
////                startActivity(intent)
//
//            },
//            { Log.e("AmplifyQuickstart", "Failed to fetch auth session") }
//        )
        Amplify.Auth.fetchUserAttributes(
                { attributes: List<AuthUserAttribute?> -> Log.i(
                        "AuthDemo",
                        "User attributes = ${attributes.toString()}"
                )
                    val intent = Intent(this, HomePage::class.java )
                    startActivity(intent)
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
        setContentView(R.layout.activity_main)
    }

    fun onbuttonClick( view: View) {
        Amplify.Auth.signInWithWebUI(this,
            { Log.i("AuthQuickStart", "Signin OK = $it")
                Thread.sleep(1_000)
                val intent = Intent(this, HomePage::class.java )
                startActivity(intent)

            },
            { Log.e("AuthQuickStart", "Signin failed", it) }
        )
//        val viewIntent = Intent(Intent.ACTION_VIEW, Uri.parse(" https://yournexttrail-dev.auth.us-east-1.amazoncognito.com/"))
       // startActivity(viewIntent);
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AWSCognitoAuthPlugin.WEB_UI_SIGN_IN_ACTIVITY_CODE) {
            Amplify.Auth.handleWebUISignInResponse(data)

        }
    }





}