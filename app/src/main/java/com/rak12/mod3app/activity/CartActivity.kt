package com.rak12.mod3app.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.rak12.mod3app.R
import com.rak12.mod3app.adapter.CartRecyclerAdapter
import com.rak12.mod3app.database.CartEntity
import com.rak12.mod3app.database.Database
import com.rak12.mod3app.database.MIGRATION_1_2


class CartActivity : AppCompatActivity() {
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerOrders: RecyclerView
    lateinit var toolbar: Toolbar
    lateinit var recyclerAdapter : CartRecyclerAdapter

    lateinit var btnPlaceOrder: Button
    lateinit var txtResName: TextView
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        toolbar=findViewById(R.id.carttoolbar)
        setuptoolbar()
        title="My Cart"
        recyclerOrders=findViewById(R.id.recyclerOrders)
        layoutManager=LinearLayoutManager(this)
        txtResName=findViewById(R.id.txtResName)
        txtResName.text="${intent.getStringExtra("nameofrest")}"
        val display:List<CartEntity> = Display(this).execute().get()
        recyclerAdapter = CartRecyclerAdapter(this,display)
        recyclerOrders.layoutManager = layoutManager
        recyclerOrders.adapter = recyclerAdapter

    }

    class Display(val context: Context) : AsyncTask<Void, Void, List<CartEntity>>() {
        override fun doInBackground(vararg params: Void?): List<CartEntity> {
            val db = Room.databaseBuilder(context, Database::class.java, "db").addMigrations(
                MIGRATION_1_2
            ).build()
            return db.cartdao().getall()

        }
    }
    fun setuptoolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }
}