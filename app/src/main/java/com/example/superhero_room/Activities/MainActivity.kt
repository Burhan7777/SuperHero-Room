package com.example.superhero_room.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.superhero_room.Adapter.SuperHeroAdapter
import com.example.superhero_room.R
import com.example.superhero_room.Room.SuperheroEntity
import com.example.superhero_room.ViewModels.MainActivityViewModel
import com.example.superhero_room.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: SuperHeroAdapter
    lateinit var viewModel: MainActivityViewModel
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var superheros = arrayListOf("Batman", "Superman", "Wonder Woman")
        var images = arrayListOf(
            getDrawable(R.drawable.batman),
            getDrawable(R.drawable.superman),
            getDrawable(R.drawable.wonderwoman)
        )

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        adapter = SuperHeroAdapter(superheros, images, this)
        binding.mainRecyclerView.adapter = adapter
        binding.mainRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        coroutineScope.launch {
            //   addToDatabase()
        }

    }

    private suspend fun addToDatabase() {
        val supermanEntity: SuperheroEntity = SuperheroEntity(
            0,
            "Superman",
            "This is superman",
            "190 cm",
            "106 kg",
            "Kryptonian",
            /*  arrayOf(
                  "Healing",
                  "Electricity resistance",
                  "Radiation absorption",
                  "Laser eyes",
                  "Flight"
              ),*/
            "90",
            "100",
            "100",
            "100",
            "70", "random"
        )
        viewModel.insertSuperHero(supermanEntity)
    }
}