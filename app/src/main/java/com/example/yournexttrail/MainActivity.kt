package com.example.yournexttrail

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.amplifyframework.AmplifyException
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.AWSDataStorePlugin
import com.amplifyframework.logging.AndroidLoggingPlugin
import com.amplifyframework.logging.LogLevel
import com.amplifyframework.predictions.aws.AWSPredictionsPlugin


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        val colorDrawable = ColorDrawable(Color.parseColor("#FF018786"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)

        try {
            // Add this line, to include the Auth plugin.
            Amplify.addPlugin(AndroidLoggingPlugin(LogLevel.VERBOSE))
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

        Amplify.Auth.fetchAuthSession(
            { Log.i("AmplifyQuickstart", "Auth session = $it")
//                Thread.sleep(1_000)
//                val intent = Intent(this, Firstpage::class.java )
//                startActivity(intent)

            },
            { Log.e("AmplifyQuickstart", "Failed to fetch auth session") }
        )
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