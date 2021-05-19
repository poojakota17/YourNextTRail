package com.example.yournexttrail

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread
import com.amplifyframework.api.ApiException
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.UserAttribute
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


class HomePage : AppCompatActivity() {


    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val view : View = LayoutInflater.from(this).inflate(R.layout.activity_home_page,frameLayout)
        setContentView(R.layout.activity_home_page)
        //super.onCreateDrawer(savedInstanceState)
     //   supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getLikings()


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
                            .apply { text = todo.name }

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