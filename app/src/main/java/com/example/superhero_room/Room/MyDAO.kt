package com.example.superhero_room.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MyDAO {

    @Insert
    suspend fun addSuperHero(superheroEntity: SuperheroEntity)

    @Query("select * from superheroDB where name ==:name")
    suspend fun getDetails(name: String) : SuperheroEntity?
}