package com.example.superhero_room.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superhero_room.Repositories.MainDBRepository
import com.example.superhero_room.Room.MyRoomDatabase
import com.example.superhero_room.Room.SuperheroEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel constructor(applicationContext: Application) :
    AndroidViewModel(applicationContext) {

    private var myRoomDatabase: MyRoomDatabase? = MyRoomDatabase.getDBInstance(applicationContext)
    private var mainDBRepository: MainDBRepository = MainDBRepository(myRoomDatabase)

    suspend fun insertSuperHero(superheroEntity: SuperheroEntity) {
        mainDBRepository.insertSuperHero(superheroEntity)
    }
}