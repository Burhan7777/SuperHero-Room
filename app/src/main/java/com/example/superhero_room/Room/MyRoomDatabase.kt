package com.example.superhero_room.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SuperheroEntity::class], version = 1)
abstract class MyRoomDatabase : RoomDatabase() {

    abstract fun getDao(): MyDAO

    companion object {
        private var myRoomDatabase: MyRoomDatabase? = null
        fun getDBInstance(context: Context): MyRoomDatabase? {
            return if (myRoomDatabase == null) {
                myRoomDatabase =
                    Room.databaseBuilder(
                        context.applicationContext,
                        MyRoomDatabase::class.java,
                        "superheroDB"
                    )
                        .allowMainThreadQueries().build()
                myRoomDatabase
            } else
                myRoomDatabase
        }
    }


}