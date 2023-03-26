package com.example.superhero_room.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MyDAO {

    @Insert
    suspend fun addSuperHero(superheroEntity: SuperheroEntity)

    @Query("select * from superheroDB where name ==:name")
    suspend fun getDetails(name: String): SuperheroEntity?

    @Query("Select name from superheroDB")
    suspend fun getNames(): List<String>?

    @Query("Select superheroImages from superheroDB")
    suspend fun getImages(): List<String>?

    @Query("delete from superheroDB where name ==:name")
    suspend fun deleteSuperHero(name: String)
}