package com.rak12.mod3app.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface dao {
    @Insert
    fun insert(restaurantEntity: RestaurantEntity)

    @Delete
    fun delete(restaurantEntity: RestaurantEntity)

    @Query("SELECT * FROM RESTAURANT")
    fun getall():List<RestaurantEntity>

    @Query("SELECT * FROM RESTAURANT WHERE ID=:id")
    fun getbyid(id:Int):RestaurantEntity

}