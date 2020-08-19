package com.rak12.mod3app.adapter

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.rak12.mod3app.R
import com.rak12.mod3app.database.Database
import com.rak12.mod3app.database.RestaurantEntity
import com.rak12.mod3app.model.Restaurant
import com.squareup.picasso.Picasso

class FavAdapter(val context: Context,val list: List<RestaurantEntity>):RecyclerView.Adapter<FavAdapter.FavViewholde>() {
    class FavViewholde(view: View):RecyclerView.ViewHolder(view){
        var restname: TextView =view.findViewById(R.id.txtRestaurantName)
        var restrating: TextView =view.findViewById(R.id.txtRestaurantRating)
        var cost: TextView =view.findViewById(R.id.txtCostForTwo)
        var img: ImageView =view.findViewById(R.id.imgRestaurantThumbnail)
        var imgfav: ImageView =view.findViewById(R.id.imgIsFav)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewholde {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_rest,parent,false)
        return FavViewholde(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: FavViewholde, position: Int) {
        var info: RestaurantEntity =list[position]
        holder.restname.text=info.name
        holder.cost.text="Rs ${info.cost_for_one}/person"
        holder.restrating.text=info.rating
        Picasso.get().load(info.image_url).error(R.drawable.food).into(holder.img)
        val restaurantEntity=RestaurantEntity(info.id,info.name,info.rating,info.cost_for_one,info.image_url)
        val checkfav= Allrestadapter.DBAsynctask(context, restaurantEntity, 1).execute().get()
        if(checkfav){
            holder.imgfav.setBackgroundResource(R.drawable.ic_action_favcolouers)
        }
        else{
            holder.imgfav.setBackgroundResource(R.drawable.ic_action_fav)
        }
//        holder.imgfav.setOnClickListener {
//            if(!DBAsynctask(context,restaurantEntity,1).execute().get()){
//                val addtofav=  DBAsynctask(context,restaurantEntity,2).execute().get()
//                if (addtofav) {
//                    Toast.makeText(context, "ADDED TO FAVS", Toast.LENGTH_SHORT).show()
//
//                    holder.imgfav.setBackgroundResource(R.drawable.ic_action_favcolouers)
//                } else {
//                    Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show()
//
//                }
//            }
//            else {
//                val removefav = DBAsynctask(context, restaurantEntity, 3).execute().get()
//                if (removefav) {
//                    Toast.makeText(context, "REMOVED FROM FAVS", Toast.LENGTH_SHORT).show()
//
//                    holder.imgfav.setBackgroundResource(R.drawable.ic_action_fav)
//
//                }
//                else {
//                    Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show()
//
//                }
//
//
//            }
//        }
//
//    }
    class DBAsynctask(val context: Context,val restaurantEntity: RestaurantEntity,val mode:Int):
        AsyncTask<Void, Void, Boolean>(){
        val db= Room.databaseBuilder(context, Database::class.java,"db").build()
        override fun doInBackground(vararg params: Void?): Boolean
        {
            when(mode){
                1->{
                    val restaurantEntity1=  db.restdao().getbyid(restaurantEntity.id)
                    db.close()
                    return restaurantEntity1!=null
                }
                2->{
                    db.restdao().insert(restaurantEntity)
                    db.close()
                    return true
                }
                3->{
                    db.restdao().delete(restaurantEntity)
                    db.close()
                    return true
                }
            }
            return false
        }


    }
}
}