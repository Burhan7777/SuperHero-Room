package com.example.superhero_room.Activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.graphics.drawable.toBitmap
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
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: SuperHeroAdapter
    lateinit var viewModel: MainActivityViewModel
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var superheros = arrayListOf("Batman", "Superman", "Wonder Woman", "Flash")
        var images = arrayListOf(
            getDrawable(R.drawable.batman),
            getDrawable(R.drawable.superman),
            getDrawable(R.drawable.wonderwoman),
            getDrawable(R.drawable.flash)
        )

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        adapter = SuperHeroAdapter(superheros, images, this)
        binding.mainRecyclerView.adapter = adapter
        binding.mainRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        coroutineScope.launch {
            addToDatabase()
            val bitmapBatman = BitmapFactory.decodeResource(resources, R.drawable.batman)
            val bitmapSuperman = BitmapFactory.decodeResource(resources, R.drawable.superman)
            val bitmapWonderWoman = BitmapFactory.decodeResource(resources, R.drawable.wonderwoman)
            val bitmapFlash = BitmapFactory.decodeResource(resources, R.drawable.flash)

            addImagesToInternalStorage("Superman.jpg", bitmapSuperman);
            addImagesToInternalStorage("Batman.jpg", bitmapBatman)
            addImagesToInternalStorage("Wonder Woman.jpg", bitmapWonderWoman)
            addImagesToInternalStorage("Flash.jpg", bitmapFlash)
        }

        binding.addSuperHeroActivityFAB.setOnClickListener {
            startActivity(Intent(this, AddSuperHeroActivity::class.java))
        }

    }

    private suspend fun addToDatabase() {
        val supermanEntity: SuperheroEntity = SuperheroEntity(
            0, "Superman", getString(R.string.superman_description), "190 cm", "106 kg",
            /*  arrayOf(
                  "Healing",
                  "Electricity resistance",
                  "Radiation absorption",
                  "Laser eyes",
                  "Flight"
              ),*/
            90F, 100F, 100F, 100F, 100F, "random"
        )
        viewModel.insertSuperHero(supermanEntity)

        val batmanEntity = SuperheroEntity(
            0,
            "Batman",
            getString(R.string.batman_description),
            "188 cm",
            "95 kg",
            100F,
            40F,
            29F,
            19F,
            100F,
            "random"
        )
        viewModel.insertSuperHero(batmanEntity)

        val wonderWomanEntity = SuperheroEntity(
            0,
            "Wonder Woman",
            getString(R.string.wonderWoman_description),
            "185 cm",
            "77 kgs",
            75F,
            90F,
            85F,
            100F,
            65F,
            "random"
        )

        viewModel.insertSuperHero(wonderWomanEntity)

        val flashEntity = SuperheroEntity(
            0,
            "Flash",
            getString(R.string.flash_description),
            "184 cm",
            "90 kgs",
            100F,
            85F,
            100F,
            80F,
            80F,
            "random"
        )
        viewModel.insertSuperHero(flashEntity)
    }

    private fun addImagesToInternalStorage(name: String, bitmap: Bitmap) {
        openFileOutput(name, MODE_PRIVATE).use {
            var byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            var byteArray = byteArrayOutputStream.toByteArray()
            it.write(byteArray)
        }
    }
}