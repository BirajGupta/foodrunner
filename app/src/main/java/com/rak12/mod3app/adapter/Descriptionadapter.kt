package com.rak12.mod3app.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rak12.mod3app.R
import com.rak12.mod3app.database.*
import com.rak12.mod3app.model.RestaurantDescription
import com.rak12.mod3app.database.MIGRATION_1_2

class Descriptionadapter(val context: Context,val Restaurantlist:ArrayList<RestaurantDescription>):RecyclerView.Adapter<Descriptionadapter.RVH>() {
    class RVH(view: View):RecyclerView.ViewHolder(view){
        val id:TextView=view.findViewById(R.id.id)
        val name:TextView=view.findViewById(R.id.txtname)
        val price:TextView=view.findViewById(R.id.cost)
        val add:Button=view.findViewById(R.id.btnadd)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVH {
       val view= LayoutInflater.from(parent.context).inflate(R.layout.singlerestdetail,parent,false)
        return RVH(view)
    }

    override fun getItemCount(): Int {
        return Restaurantlist.size
    }


    override fun onBindViewHolder(holder: RVH, position: Int) {
        val data=Restaurantlist[position]

        holder.id.text=(position+1).toString()
        holder.name.text=data.name
        holder.price.text="Rs${data.cost}"
        val cartEntity=CartEntity(data.id,data.restaurantid,data.name,data.cost)
        var checkadded=Ordercarttable(context,cartEntity,1).execute()
        var ispresent=checkadded.get()
        if(ispresent){
            holder.add.setBackgroundResource(R.color.yellow)
            holder.add.text="REMOVE"
            //holder.add.setTextColor(R.color.white)


        }
        else{
            holder.add.setBackgroundResource(R.color.colorPrimary)
            holder.add.text="ADD"
           // holder.add.setTextColor(R.color.white)
        }
        holder.add.setOnClickListener {
            if(!Ordercarttable(context,cartEntity,1).execute().get()){
                var add=Ordercarttable(context,cartEntity,2).execute()
                var check=add.get()
                if(check){
                    holder.add.setBackgroundResource(R.color.yellow)
                    holder.add.text="REMOVE"
                    Toast.makeText(context,"Item added in cart",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context,"Some error occured",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                val remove=Ordercarttable(context,cartEntity,3).execute().get()
                if(remove){ holder.add.setBackgroundResource(R.color.colorPrimary)
                    holder.add.text="ADD"
                    Toast.makeText(context,"Item removed from cart",Toast.LENGTH_SHORT).show()

                }
                else{
                    Toast.makeText(context,"Some error occured",Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    class Ordercarttable(val context: Context,val cartEntity: CartEntity,val mode:Int):AsyncTask<Void,Void,Boolean>(){
        val db2=Room.databaseBuilder(context,Database::class.java,"db2").addMigrations(MIGRATION_1_2).build()
        override fun doInBackground(vararg params: Void?): Boolean {
            when(mode)
            {
                1->{
                    val cartEntity1 : CartEntity ?= db2.cartdao().getbyid2(cartEntity.foodname)
                    db2.close()
                    return cartEntity1!=null
                }
                2->{
                    db2.cartdao().insertfooditem(cartEntity)
                    db2.close()
                    return true
                }
                3->{
                    db2.cartdao().deletefooditem(cartEntity)
                    db2.close()
                    return true
                }

            }
            return false
        }

    }
}