package com.rak12.mod3app.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Cartdao {
    @Insert
    fun insertfooditem(cartEntity: CartEntity)

    @Delete
    fun deletefooditem(cartEntity: CartEntity)

    @Query("SELECT * FROM cartorder")
    fun getall():List<CartEntity>

    @Query("SELECT * FROM cartorder WHERE FOODNAME=:foodname ")
    fun getbyid2(foodname:String):CartEntity


    @Query("DELETE FROM cartorder;")
    fun deleteAllEntries()
}