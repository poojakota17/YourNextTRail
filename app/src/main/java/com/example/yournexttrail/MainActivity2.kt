package com.example.yournexttrail

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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


class MainActivity2 : AppCompatActivity() {
    var trailid : String=""
    lateinit var  mAdaptter: ReviewListAdapter
    lateinit var result: ArrayList<Reviews>
    var updated: Boolean = false
    lateinit var myRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        myRecyclerView = findViewById(R.id.myRecyclerView2)

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

    fun savereview(view: View) {
        val review : EditText= findViewById(R.id.review)
        val str : String
        val e= PreferenceManager.getDefaultSharedPreferences(this).getString("email", "default value")
        val userid = PreferenceManager.getDefaultSharedPreferences(this).getString("userid", "default value")
        Log.i("email",e!!)
        str= review.text.toString()

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