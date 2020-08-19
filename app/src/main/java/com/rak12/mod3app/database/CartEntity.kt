package com.rak12.mod3app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cartorder")
data class CartEntity
    (@ColumnInfo(name = "foodid")  val foodid:String,
     @ColumnInfo(name = "resid")  val resid:Int,
    @ColumnInfo(name="foodname")@PrimaryKey val foodname:String,
    @ColumnInfo(name = "price") val price:String
)