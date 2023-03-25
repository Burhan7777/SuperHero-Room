package com.example.superhero_room.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Base64

@Entity(tableName = "superheroDB")
data class SuperheroEntity constructor(
    @PrimaryKey(autoGenerate = true)
    var primaryKey: Long = 0,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "height")
    var height: String,

    @ColumnInfo(name = "weight")
    var weight: String,


    /*@ColumnInfo(name = "powers")
    private val powers: Array<String>,*/

    @ColumnInfo(name = "intelligence")
    var intelligence: Float,

    @ColumnInfo(name = "strength")
    var strength: Float,

    @ColumnInfo(name = "speed")
    var speed: Float,

    @ColumnInfo(name = "durability")
    var durability: Float,

    @ColumnInfo(name = "combat")
    var combat: Float,

    @ColumnInfo(name = "superheroImages")
    var superheroImages: String
) {

    // Entity class of superhero DB
}