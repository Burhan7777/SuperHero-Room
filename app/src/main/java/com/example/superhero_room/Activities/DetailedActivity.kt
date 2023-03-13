package com.example.superhero_room.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.superhero_room.Room.SuperheroEntity
import com.example.superhero_room.ViewModels.DetailedActivityViewModel
import com.example.superhero_room.databinding.ActivityDetailedBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailedActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailedBinding
    lateinit var viewModel: DetailedActivityViewModel
    private var coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    var superheroEntity: SuperheroEntity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[DetailedActivityViewModel::class.java]
        coroutineScope.launch {
            superheroEntity = viewModel.getDetails("Superman")
            withContext(Dispatchers.Main) {
                binding.description.text = superheroEntity?.description

            }
        }

    }
}