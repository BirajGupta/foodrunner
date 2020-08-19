package com.rak12.mod3app.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.rak12.mod3app.R
import com.rak12.mod3app.adapter.Descriptionadapter
import com.rak12.mod3app.database.*
import com.rak12.mod3app.model.RestaurantDescription
import com.rak12.mod3app.util.ConnectionManager
import org.json.JSONException
import com.rak12.mod3app.database.MIGRATION_1_2

class RestaurantDetails : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var recyclerView2: RecyclerView
    lateinit var layout:RecyclerView.LayoutManager
    lateinit var proceed: Button
    lateinit var adapter:Descriptionadapter
    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_details)
        toolbar=findViewById(R.id.tool)
        recyclerView2=findViewById(R.id.rv2)
        layout=LinearLayoutManager(this)
        proceed=findViewById(R.id.place)
        var dlist= arrayListOf<RestaurantDescription>()
        setuptoolbar()
        title="${intent.getStringExtra("nameofrest")}"
       // proceed.visibility=View.INVISIBLE
        val que= Volley.newRequestQueue(this)
        val url="http://13.235.250.119/v2/restaurants/fetch_result/${intent.getIntExtra("rid",100)}"
        if (ConnectionManager().checkconnectivity(this)){
            val jsonObjectRequest=object : JsonObjectRequest(Method.GET,url,null, Response.Listener {
                try {

                    val data = it.getJSONObject("data")
                    val boolean=data.getBoolean("success")
                    if(boolean){
                        val data2=data.getJSONArray("data")
                        for(i in 0 until data2.length()){
                            val jsonObject =data2.getJSONObject(i)
                            val restaurant= RestaurantDescription(jsonObject.getString("id"),
                                jsonObject.getString("name"),
                                jsonObject.getString("cost_for_one"),
                                jsonObject.getInt("restaurant_id")
                            )
                            dlist.add(restaurant)


                            adapter= Descriptionadapter(this,dlist)

                            recyclerView2.adapter = adapter
                            recyclerView2.layoutManager = layout
                        }

                    }
                    else{
                        Toast.makeText(this,"ERROR", Toast.LENGTH_LONG).show()
                    }

                }catch (e: JSONException){
                    Toast.makeText(this,"ERROR 1213", Toast.LENGTH_LONG).show()
                }

            }, Response.ErrorListener {
                Toast.makeText(this,"VOLLEY ERROR", Toast.LENGTH_LONG).show()
            })
            {
                override fun getHeaders(): MutableMap<String, String>
                {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "b239d60302e428"
                    return headers
                }

            }
            que.add(jsonObjectRequest)
                proceed.setOnClickListener {
                    val x=intent.getStringExtra("nameofrest")
                    val intent = Intent(this, CartActivity::class.java)
                    intent.putExtra("nameofrest",x)
                    startActivity(intent)
                    }


        }
        else{
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Error")
            alert.setMessage("INTERNET connection not found")
            alert.setPositiveButton("open settings") { text, listener ->
                val i= Intent(Settings.ACTION_WIFI_SETTINGS)
                startActivity(i)
                finish()


            }
            alert.setNegativeButton("exit") { text, listener ->
                ActivityCompat.finishAffinity(this)

            }
            alert.create().show()



        }


    }
    fun setuptoolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }
    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }


    override fun onBackPressed(){

        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Confirmation")
        dialog.setMessage("Going back will reset items. Do you still want to proceed?")
        dialog.setPositiveButton("YES"){text,listener->

           var checkdeleted= DeleteAll(this).execute().get()
            if(checkdeleted){

            //Volley.newRequestQueue(this).cancelAll(this::class.java.simpleName)
            val intent = Intent(this, DashboardActivity:: class.java)
            startActivity(intent)
            finish()}
            else{
                Toast.makeText(this,"ERROR", Toast.LENGTH_LONG).show()
            }
        }
        dialog.setNegativeButton("NO"){text,listener->

        }
        dialog.create()
        dialog.show()

    }


    class DeleteAll(val context: Context) : AsyncTask<Void, Void, Boolean>() {



        val db2 = Room.databaseBuilder(context, Database::class.java, "CartOrders")
            .addMigrations(
                MIGRATION_1_2
            ).build()

        override fun doInBackground(vararg params: Void?): Boolean {


            db2.cartdao().deleteAllEntries()
            db2.close()
            return true
        }
    }

}