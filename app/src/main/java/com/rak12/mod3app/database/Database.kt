package com.rak12.mod3app.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [RestaurantEntity::class,CartEntity::class],version = 2)
abstract class Database: RoomDatabase() {
    abstract fun restdao():dao
    abstract fun cartdao():Cartdao
}

//val MIGRATION_1_2: Migration = object : Migration(1, 2) {
//    override fun migrate(database: SupportSQLiteDatabase) {
//        database.execSQL("CREATE TABLE cartorder(resid INTEGER NOT NULL,foodname TEXT Primary Key NOT NULL,price TEXT NOT NULL )")
//    }
//}
//val MIGRATION_2_3: Migration = object : Migration(2, 3) {
//    override fun migrate(database: SupportSQLiteDatabase) {
//        database.execSQL(
//            "ALTER TABLE cartorder ADD COLUMN foodid TEXT"
//        )
//    }
//}
val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE cartorder(foodid TEXT  NOT NULL,resid INTEGER NOT NULL,foodname TEXT PRIMARY KEY NOT NULL,price TEXT NOT NULL )"
        )
    }
}