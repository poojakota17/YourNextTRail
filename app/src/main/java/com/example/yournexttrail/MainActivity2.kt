package com.example.yournexttrail

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amplifyframework.api.ApiException
import com.amplifyframework.api.graphql.GraphQLResponse
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.model.Model
import com.amplifyframework.datastore.generated.model.Reviews
import com.amplifyframework.datastore.generated.model.Trail
import com.amplifyframework.datastore.generated.model.User
import com.amplifyframework.datastore.generated.model.UserAttribute
import com.amplifyframework.predictions.PredictionsException
import com.amplifyframework.predictions.result.InterpretResult
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity2 : AppCompatActivity() {
    var trailid : String=""
    lateinit var  mAdaptter: ReviewListAdapter
    lateinit var result: ArrayList<Reviews>
    lateinit var bottomNavigation :BottomNavigationView

    var updated: Boolean = false
    lateinit var myRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
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
        myRecyclerView = findViewById(R.id.myRecyclerView2)
//        val itemDecor = DividerItemDecoration(applicationContext, ClipDrawable.HORIZONTAL)
//        itemDecor.setDrawable(getDrawable(R.drawable.divider)!!)
//        myRecyclerView.addItemDecoration(itemDecor)

        myRecyclerView.layoutManager= LinearLayoutManager(this)
        val title=intent.getStringExtra("title")
        val description= intent.getStringExtra("description")
        val image= intent.getStringExtra("image")
        val  level=intent.getStringExtra("level")
        val name : TextView = findViewById(R.id.title)
        name.text=title
        val desc : TextView = findViewById(R.id.description)
        desc.text=description
        val le : TextView=findViewById(R.id.level)
        le.text=level
        val url : ImageView = findViewById(R.id.image)
        trailid= intent.getStringExtra("id").toString()
        Glide.with(url.context).load(image).into(url)

        mAdaptter= ReviewListAdapter()
        getreviews()
        while(!updated){
            Thread.sleep(500)
        }
        Log.i("result size", result.size.toString())
//       updated=false

        // mAdaptter.updateitem(result)
        // myRecyclerView.adapter = mAdaptter
        updated = false

    }


    fun getreviews(){
        result = ArrayList()
        Amplify.API.query(
            ModelQuery.get(Trail::class.java, trailid),
            { response ->
                response.data.reviews.forEach(){
                result.add(it)
            }
                Log.i("result_size_on_success", result.size.toString())
                mAdaptter.updateitem(result)
                runOnUiThread(){
                    myRecyclerView.adapter = mAdaptter
                }

                updated = true
                Log.i(
                    "trail_title",
                    (response.data.title)
                )
            }
        ) { error: ApiException ->
            Log.e(
                "MyAmplifyApp",
                error.toString(),
                error
            )
        }

    }
    fun alert(view : View) {
        val input = EditText(this@MainActivity2)
        val lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            savereview(input.text.toString())

           Thread.sleep(500)
           // getreviews()
            Toast.makeText(
                applicationContext,
                "Review Saved", Toast.LENGTH_SHORT
            ).show()

        }
        val negativeButtonClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(
                applicationContext,
                "Cancel", Toast.LENGTH_SHORT
            ).show()

        }
        val builder = AlertDialog.Builder(this)
        with(builder)
        {
            setTitle("Write a review")
            //setMessage(str)
            setView(input)
            setIcon(R.drawable.ic_baseline_rate_review_24)
            setPositiveButton("SAVE", DialogInterface.OnClickListener(function = positiveButtonClick))
            setNegativeButton("CANCEL",DialogInterface.OnClickListener(function = negativeButtonClick))
            show()
        }
    }
    fun savereview(rev : String) {
       // val review : EditText= findViewById(R.id.review)
        val str : String
        val e= PreferenceManager.getDefaultSharedPreferences(this).getString("email", "default value")
        val userid = PreferenceManager.getDefaultSharedPreferences(this).getString("userid", "default value")
        Log.i("email",e!!)
        //str= review.text.toString()
        str=rev
        Amplify.Predictions.interpret(
            str,
            { result: InterpretResult ->
                Log.i(
                    "MyAmplifyApp",
                    result.sentiment!!.value.toString()
                )
                var sentiment=result.sentiment!!.value.toString()

                val userreview= Reviews.builder()
                    .review(str)
                    .senitment(result.sentiment!!.value.toString())
                    .user(User.justId(userid))
                    .trail(Trail.justId(trailid))
                    .build()
                Amplify.API.mutate(
                    ModelMutation.create(userreview),
                    { response ->
                        //Toast.makeText(this,"Thanks for your contribution, review saved",Toast.LENGTH_LONG).show()
                        Log.i(
                            "Reviewresponse",
                            "Todo with id: " + response.data.id
                        )

                    }
                ) { error: ApiException? ->
                    Log.e(
                        "MyAmplifyApp",
                        "Create failed",
                        error
                    )
                }
                var nounList : MutableMap<String,Int>  = mutableMapOf()
                if(sentiment.equals("POSITIVE")){
                    for (adj in result.syntax!!) {
                        if(adj.value.toString() == "NOUN") {
                            var targetText: String = adj.targetText
                            nounList.put(targetText, 1)
                        }
                        Log.i("syntax",adj.targetText + adj.value)
                    }
                    Amplify.API.query(
                        ModelQuery.list(UserAttribute::class.java, UserAttribute.EMAIL.contains(e)),
                        { response ->
                            response.data.forEach { todo ->
                                if(nounList.containsKey(todo.name)){
                                    val userattr = UserAttribute.builder()
                                        .name(todo.name)
                                        .score(todo.score+1)
                                        .email(e)
                                        .id(todo.id)
                                        .build()
                                    Log.i("tryAttributeOpr", "update")
                                    updateRecord(userattr, "UPDATE")
                                    //Thread.sleep(3_000)
                                    nounList.remove(todo.name)

                                }

                            }
                            if(nounList.isNotEmpty())
                            {
                               nounList.forEach{ k,v ->
                                   val userattr = UserAttribute.builder()
                                       .name(k)
                                       .score(1)
                                       .email(e)
                                       .build()
                                   Log.i("tryAttributeOpr", "create")
                                updateRecord(userattr, "CREATE")
                                   //Thread.sleep(3_000)
                               }
                            }
                        },
                        { Log.e("AttributeFilter", "Query failure", it) }
                    )
                }

            }
        ) { error: PredictionsException? ->
            Log.e(
                "MyAmplifyApp",
                "Interpret failed",
                error
            )
        }

    }

    fun updateRecord(userattr: UserAttribute, operation: String){
        if (operation.equals("UPDATE")) {
            Amplify.API.mutate(
                ModelMutation.update<Model>(
                    userattr
                ),
                { response: GraphQLResponse<Model> ->
                    Log.i(
                        " updateattributes",
                        "Todo with id: " + response.data.id
                    )
                }
            ) { error: ApiException? ->
                Log.e(
                    "NounUpdate",
                    "Update failed",
                    error
                )
            }
        } else {
            Amplify.API.mutate(
                ModelMutation.create<Model>(
                    userattr
                ),
                { response: GraphQLResponse<Model> ->
                    Log.i(
                        "insertattribute",
                        "Todo with id: " + response.data.id
                    )
                }
            ) { error: ApiException? ->
                Log.e(
                    "NounCreate",
                    "Create failed",
                    error
                )
            }
        }

    }

}