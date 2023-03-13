package com.example.superhero_room.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.superhero_room.Repositories.MainDBRepository
import com.example.superhero_room.Room.MyRoomDatabase
import com.example.superhero_room.Room.SuperheroEntity

class DetailedActivityViewModel(application: Application) : AndroidViewModel(application) {
    var myRoomDatabase: MyRoomDatabase? = MyRoomDatabase.getDBInstance(application)
    var mainDBRepository: MainDBRepository = MainDBRepository(myRoomDatabase)

    suspend fun getDetails(name: String): SuperheroEntity?{
       return mainDBRepository.getDetails(name)
    }
}