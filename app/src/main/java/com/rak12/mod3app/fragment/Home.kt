package com.rak12.mod3app.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.rak12.mod3app.R
import com.rak12.mod3app.adapter.Allrestadapter
import com.rak12.mod3app.model.Restaurant
import com.rak12.mod3app.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject


class Home : Fragment() {

    lateinit var rv: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var adapter:Allrestadapter
    lateinit var pl:RelativeLayout
    val restlist= arrayListOf<Restaurant>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_home, container, false)
        rv=view.findViewById(R.id.allrestrecycler)
        pl=view.findViewById(R.id.pl)

        layoutManager=LinearLayoutManager(activity)
        pl.visibility=View.VISIBLE
        val que= Volley.newRequestQueue(activity as Context)
        val url="http://13.235.250.119/v2/restaurants/fetch_result"
        if (ConnectionManager().checkconnectivity(activity as Context)){
        val jsonObjectRequest=object : JsonObjectRequest(Method.GET,url,null,Response.Listener {
            try {
                pl.visibility=View.GONE
                val data = it.getJSONObject("data")
                val boolean=data.getBoolean("success")
                if(boolean){
                        val data2=data.getJSONArray("data")
                    for(i in 0 until data2.length()){
                        val jsonObject =data2.getJSONObject(i)
                        val restaurant=Restaurant(
                            jsonObject.getInt("id"),
                            jsonObject.getString("name"),
                            jsonObject.getString("rating"),
                            jsonObject.getString("cost_for_one"),
                            jsonObject.getString("image_url")
                        )
                        restlist.add(restaurant)



                    }
                    adapter= Allrestadapter(activity as Context,restlist)


                    rv.adapter = adapter
                    rv.layoutManager = layoutManager

                }
                else{
                    Toast.makeText(activity as Context,"ERROR", Toast.LENGTH_LONG).show()
                }

            }catch (e:JSONException){
                Toast.makeText(activity as Context,"ERROR 1213",Toast.LENGTH_LONG).show()
            }

        }, Response.ErrorListener {
            Toast.makeText(activity as Context,"VOLLEY ERROR",Toast.LENGTH_LONG).show()
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
        }
        else{
            val alert = AlertDialog.Builder(activity as Context)
            alert.setTitle("Error")
            alert.setMessage("INTERNET connection not found")
            alert.setPositiveButton("open settings") { text, listener ->
                val i= Intent(Settings.ACTION_WIFI_SETTINGS)
                startActivity(i)
                activity?.finish()


            }
            alert.setNegativeButton("exit") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)

            }
            alert.create().show()



        }

        return view




    }
}