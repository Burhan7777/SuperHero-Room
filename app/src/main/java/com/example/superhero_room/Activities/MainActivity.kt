package com.example.superhero_room.Activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
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
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

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

        images = ArrayList()


        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        //  binding.progressBarMainActivity.visibility = View.VISIBLE

        Toast.makeText(this, "Loading data from database", Toast.LENGTH_SHORT).show()

        coroutineScope.launch {
            var job = launch {

                bitmapBatman = BitmapFactory.decodeResource(resources, R.drawable.batman)
                bitmapSuperman = BitmapFactory.decodeResource(resources, R.drawable.superman)
                bitmapWonderWoman = BitmapFactory.decodeResource(resources, R.drawable.wonderwoman)
                bitmapFlash = BitmapFactory.decodeResource(resources, R.drawable.flash)

                // moveDBFromAssetsToInternalStorage()

                if (!ifDBExistsOrNot())
                    addToDatabase()



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
                    StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            }
        }


        // binding.progressBarMainActivity.visibility = View.INVISIBLE


        binding.addSuperHeroActivityFAB.setOnClickListener {
            startActivity(Intent(this, AddSuperHeroActivity::class.java))
        }

        Toast.makeText(this, "Data Loaded", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
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

    private fun ifDBExistsOrNot(): Boolean {
        var file = getDatabasePath("/data/data/com.example.superhero_room/databases/superheroDB")
        return file.exists()
    }


    // This method used to transfer database from assets folder to the database
    // folder however now ifDBExistsOrNot method checks if the database exists or not
    // if not it adds default superheros to the database otherwise it doesn't.
    // Not deleted yet , maybe used in future.


/*    private fun moveDBFromAssetsToInternalStorage() {
        var databasePath = "/data/data/com.example.superhero_room/databases/"
        var databaseName = "superheroDB"
        var databaseShm = "superheroDB-shm"
        var databaseWal = "superheroDB-wal"

        var inputStreamDB = assets.open(databaseName)
        var inputStreamShm = assets.open(databaseShm)
        var inputStreamWal = assets.open(databaseWal)

        var fileNameDB = databasePath + databaseName
        var fileNameShm = databasePath + databaseShm
        var fileNameWal = databasePath + databaseWal

        var outputStreamDB: OutputStream = FileOutputStream(fileNameDB)
        var outputStreamSHm = FileOutputStream(fileNameShm)
        var outputStreamWal = FileOutputStream(fileNameWal)

        var byteArrayDB = ByteArray(1024)
        var byteArraySHm = ByteArray(1024)
        var byteArrayWal = ByteArray(1024)

        var lengthDB = inputStreamDB.read(byteArrayDB)
        while (lengthDB > 0) {
            outputStreamDB.write(byteArrayDB, 0, lengthDB)
        }

        var lengthShm = inputStreamShm.read(byteArraySHm)
        while (lengthShm > 0) {
            outputStreamSHm.write(byteArraySHm, 0, lengthShm)
        }

        var lengthWal = inputStreamWal.read(byteArrayWal)
        while (lengthWal > 0) {
            outputStreamWal.write(byteArrayWal, 0, lengthWal)
        }

        outputStreamDB.flush()
        outputStreamDB.close()
        inputStreamDB.close()

        outputStreamSHm.flush()
        outputStreamSHm.close()
        inputStreamShm.close()

        outputStreamWal.flush()
        outputStreamWal.close()
        inputStreamWal.close()
    }*/

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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
        var byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}