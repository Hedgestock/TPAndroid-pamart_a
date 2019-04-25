package com.example.hellogames

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageIdList = listOf(imageView0,imageView1,imageView2,imageView3)
        // The base URL where the WebService is located
        val baseURL = "https://androidlessonsapi.herokuapp.com/api/"
        // Use GSON library to create our JSON parser
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        // Create a Retrofit client object targeting the provided URL
        // and add a JSON converter (because we are expecting json responses)
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(jsonConverter)
            .build()
        // Use the client to create a service:
        // an object implementing the interface to the WebService
        val service: WebServiceInterface = retrofit.create(WebServiceInterface::class.java)


        val wsCallbackList: Callback<List<GameObject>> = object : Callback<List<GameObject>> {
            override fun onFailure(call: Call<List<GameObject>>, t: Throwable) {
                // Code here what happens if calling the WebService fails
                Log.w("TAG", "WebService call failed")
            }
            override fun onResponse(call: Call<List<GameObject>>, response: Response<List<GameObject>>) {
                Log.d("TAG", "code : " + response.code())
                if (response.code() == 200) {
                    // We got our data !
                    Log.d("TAG", "We got our data !")
                    val responseData = response.body()
                    if (responseData != null) {
                        Log.d("TAG", "WebService success : " + responseData.size)
                        responseData
                        var i = 0
                        for (id in imageIdList) {
                            Glide.with(this@MainActivity)
                                .load(responseData[i].picture)
                                .into(id)
                            i++
                        }
                    }
                }
            }
        }

        /*val wsCallbackDetails: Callback<GameObject>> = object : Callback<List<GameObject>> {
            override fun onFailure(call: Call<List<GameObject>>, t: Throwable) {
                // Code here what happens if calling the WebService fails
                Log.w("TAG", "WebService call failed")
            }
            override fun onResponse(call: Call<List<GameObject>>, response: Response<List<GameObject>>) {
                if (response.code() == 200) {
                    // We got our data !
                    val responseData = response.body()
                    if (responseData != null) {
                        Log.d("TAG", "WebService success : " + responseData.size)
                        responseData
                        var i = 0
                        for (id in imageIdList) {
                            Glide.with(this@MainActivity)
                                .load(responseData[i].picture)
                                .into(id)
                            i++
                        }
                    }
                }
            }
        }*/

        service.listGames().enqueue(wsCallbackList)

    }
}

interface WebServiceInterface {
    @GET("game/list")
    fun listGames(): Call<List<GameObject>>

    @GET("game/details")
    fun gameDetails(@Query("game_id") id: Int): Call<List<GameObject>>
}
