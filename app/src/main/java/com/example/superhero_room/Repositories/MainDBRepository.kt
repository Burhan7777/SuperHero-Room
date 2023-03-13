package com.example.superhero_room.Repositories

import android.util.Log
import com.example.superhero_room.Room.MyRoomDatabase
import com.example.superhero_room.Room.SuperheroEntity

class MainDBRepository constructor(myRoomDatabase: MyRoomDatabase?) {

    var myRoomDatabase: MyRoomDatabase?

    init {
        this.myRoomDatabase = myRoomDatabase
    }

    suspend fun insertSuperHero(superheroEntity: SuperheroEntity) {
        myRoomDatabase?.getDao()?.addSuperHero(superheroEntity)
    }

    suspend fun getDetails(name: String):SuperheroEntity? {
       return myRoomDatabase?.getDao()?.getDetails(name)
    }

}