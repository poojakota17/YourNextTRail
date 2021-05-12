package com.example.yournexttrail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Log.e
import android.util.Log.i
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthUserAttribute
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            // Add this line, to include the Auth plugin.

            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.configure(applicationContext)
            Log.i("Yournexttrail", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("Yournexttrail", "Could not initialize Amplify", error)
        }
        Log.i("Hello","HELLO WORLD")
        Amplify.Auth.signInWithWebUI(this,
            { Log.i("AuthQuickStart", "Signin OK = $it")


            },
            { Log.e("AuthQuickStart", "Signin failed", it) }
        )

        Amplify.Auth.fetchAuthSession(
            { Log.i("AmplifyQuickstart", "Auth session = $it")
                val intent = Intent(this, Firstpage::class.java )
                startActivity(intent)

            },
            { Log.e("AmplifyQuickstart", "Failed to fetch auth session") }

)
        setContentView(R.layout.activity_main)
    }
    fun onbuttonClick( view: View) {
        val viewIntent = Intent(Intent.ACTION_VIEW, Uri.parse(" https://yournexttrail-dev.auth.us-east-1.amazoncognito.com/"))
        startActivity(viewIntent);
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AWSCognitoAuthPlugin.WEB_UI_SIGN_IN_ACTIVITY_CODE) {
            Amplify.Auth.handleWebUISignInResponse(data)

        }
    }





}