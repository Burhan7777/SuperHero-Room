package com.example.superhero_room.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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

    @ColumnInfo(name = "species")
    var species: String,

    /*@ColumnInfo(name = "powers")
    private val powers: Array<String>,*/

    @ColumnInfo(name = "intelligence")
    var intelligence: String,

    @ColumnInfo(name = "strength")
    var strength: String,

    @ColumnInfo(name = "speed")
    var speed: String,

    @ColumnInfo(name = "durability")
    var durability: String,

    @ColumnInfo(name = "combat")
    var combat: String,

    @ColumnInfo(name = "photoUri")
    var uri: String
) {

    // Entity class of superhero DB
}