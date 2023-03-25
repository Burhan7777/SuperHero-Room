package com.example.superhero_room.Activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.core.graphics.drawable.toDrawable
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
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: SuperHeroAdapter
    lateinit var viewModel: MainActivityViewModel
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private var drawable1: Drawable? = null
    private var images: List<String>? = null
    private lateinit var bitmapBatman: Bitmap
    private lateinit var bitmapSuperman: Bitmap
    private lateinit var bitmapWonderWoman: Bitmap
    private lateinit var bitmapFlash: Bitmap
    private var superheros: List<String>? = null
    private var superheroImages: ArrayList<Drawable>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        superheros = ArrayList()

        images = arrayListOf(
            /*AppCompatResources.getDrawable(this, R.drawable.batman),
            AppCompatResources.getDrawable(this, R.drawable.superman),
            AppCompatResources.getDrawable(this, R.drawable.wonderwoman),
            AppCompatResources.getDrawable(this, R.drawable.flash)*/
        )

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        //  binding.progressBarMainActivity.visibility = View.VISIBLE


        coroutineScope.launch {
            var job = launch {

                bitmapBatman = BitmapFactory.decodeResource(resources, R.drawable.batman)
                bitmapSuperman = BitmapFactory.decodeResource(resources, R.drawable.superman)
                bitmapWonderWoman = BitmapFactory.decodeResource(resources, R.drawable.wonderwoman)
                bitmapFlash = BitmapFactory.decodeResource(resources, R.drawable.flash)

               // addToDatabase()

                addImagesToInternalStorage("Superman.jpg", bitmapSuperman);
                addImagesToInternalStorage("Batman.jpg", bitmapBatman)
                addImagesToInternalStorage("Wonder Woman.jpg", bitmapWonderWoman)
                addImagesToInternalStorage("Flash.jpg", bitmapFlash)

                superheros = viewModel.getNames()
                images = viewModel.getImages()

                superheroImages = ArrayList()

                for (i in viewModel.getImages()!!.indices) {
                    val byteArray = Base64.decode(viewModel.getImages()!![i], Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                    superheroImages?.add(bitmap.toDrawable(resources))
                }


            }
            job.join()

            launch(Dispatchers.Main) {
                //  for (i in 0..superheros!!.size) {
                //       Log.i("names", superheroImages!!.size.toString())
                //  }

                adapter = SuperHeroAdapter(superheros, superheroImages, this@MainActivity)
                binding.mainRecyclerView.adapter = adapter
                binding.mainRecyclerView.setHasFixedSize(true)
                binding.mainRecyclerView.layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            }
        }


        //  binding.progressBarMainActivity.visibility = View.INVISIBLE


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
            90F, 100F, 100F, 100F, 100F, convertBitmapToBase64String(bitmapSuperman)
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
            convertBitmapToBase64String(bitmapBatman)
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
            convertBitmapToBase64String(bitmapWonderWoman)
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
            convertBitmapToBase64String(bitmapFlash)
        )
        viewModel.insertSuperHero(flashEntity)
    }

    private fun addImagesToInternalStorage(name: String, bitmap: Bitmap) {
        openFileOutput(name, MODE_PRIVATE).use {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            it.write(byteArray)
        }
    }

    private fun convertBitmapToBase64String(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream)
        var byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}