package com.example.superhero_room.Activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.superhero_room.Adapter.SuperHeroAdapter
import com.example.superhero_room.Room.SuperheroEntity
import com.example.superhero_room.ViewModels.AddActivityViewModel
import com.example.superhero_room.databinding.ActivityAddSuperHeroBinding
import com.github.drjacky.imagepicker.ImagePicker.Companion.with
import com.github.drjacky.imagepicker.constant.ImageProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class AddSuperHeroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddSuperHeroBinding
    private var coroutineScope = CoroutineScope(Dispatchers.IO)
    private var adapter: SuperHeroAdapter = SuperHeroAdapter()
    private var superHeroNames: ArrayList<String> = ArrayList()
    private var superHeroImages: ArrayList<Drawable> = ArrayList()
    private lateinit var viewModel: AddActivityViewModel
    private lateinit var mainActivity: MainActivity
    private lateinit var uri : Uri

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                 uri = it.data?.data!!
                Glide.with(this@AddSuperHeroActivity).load(uri).fitCenter().circleCrop()
                    .into(binding.superheroUploadImage)

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSuperHeroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[AddActivityViewModel::class.java]

        setSupportActionBar(binding.toolbar)

        mainActivity = MainActivity()

        binding.superHeroPhotoSelectionButton.setOnClickListener {
            with(this@AddSuperHeroActivity)
                .crop()
                //  .cropOval()
                // .maxResultSize(512, 512, true)
                .galleryOnly()
                .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                .createIntentFromDialog { launcher.launch(it) }
        }

        binding.saveButton.setOnClickListener {

            var isItInTheLimits = checkTheLimitsOfPower()

            if (editTextsAreEmpty()) {
                if (isItInTheLimits) {
                    coroutineScope.launch {
                        addSuperHero(
                            binding.nameOfSuperhero.text.toString(),
                            binding.descriptonOfSuperHero.text.toString(),
                            binding.heightOfSuperHero.text.toString(),
                            binding.weightOfSuperHero.text.toString(),
                            binding.intelligenceOfSuperHero.text.toString().toFloat(),
                            binding.strengthOfSuperHero.text.toString().toFloat(),
                            binding.speedOfSuperHero.text.toString().toFloat(),
                            binding.durabilityOfSuperHero.text.toString().toFloat(),
                            binding.combatOfSuperHero.text.toString().toFloat(),
                            convertImageViewToBase64String(binding.superheroUploadImage)
                        )
                    }

                    superHeroNames.add(binding.nameOfSuperhero.text.toString())
                    superHeroImages.add(binding.superheroUploadImage.drawable)

                    val bitmapToBeSaved: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri);

                    addImagesToInternalStorage(
                        binding.nameOfSuperhero.text.toString() + ".jpg",
                        bitmapToBeSaved
                    )
                    finish()
                    startActivity(Intent(this@AddSuperHeroActivity, MainActivity::class.java))
                }
            } else {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addImagesToInternalStorage(name: String, bitmap: Bitmap) {
        openFileOutput(name, MODE_PRIVATE).use {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            it.write(byteArray)
        }
    }

    private fun convertImageViewToBase64String(imageView: ImageView): String {
        var bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri);
        var byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private suspend fun addSuperHero(
        name: String, description: String, height: String, weight: String,
        intelligence: Float, strength: Float, speed: Float, durability: Float, combat: Float,
        superheroImage: String
    ) {
        val superheroEntity = SuperheroEntity(
            0,
            name, description, height, weight, intelligence,
            strength, speed, durability, combat, superheroImage
        )

        viewModel.insertSuperHero(superheroEntity)
    }

    private fun checkTheLimitsOfPower(): Boolean {
        var isItInTheLimits = true

        if (editTextsAreEmpty()) {
            if (binding.strengthOfSuperHero.text.toString()
                    .toInt() > 100 || binding.strengthOfSuperHero.text.toString().toInt() < 0
            ) {
                binding.strength.error = "Value needs to be between 0-100"
                isItInTheLimits = false
            }

            if (binding.intelligenceOfSuperHero.text.toString()
                    .toInt() > 100 || binding.intelligenceOfSuperHero.text.toString().toInt() < 0
            ) {
                binding.intelligence.error = "Value needs to be between 0-100"
                isItInTheLimits = false
            }

            if (binding.speedOfSuperHero.text.toString()
                    .toInt() > 100 || binding.speedOfSuperHero.text.toString().toInt() < 0
            ) {
                binding.speed.error = "Value needs to be between 0-100"
                isItInTheLimits = false
            }

            if (binding.durabilityOfSuperHero.text.toString()
                    .toInt() > 100 || binding.durabilityOfSuperHero.text.toString().toInt() < 0
            ) {
                binding.durability.error = "Value needs to be between 0-100"
                isItInTheLimits = false
            }

            if (binding.combatOfSuperHero.text.toString()
                    .toInt() > 100 || binding.combatOfSuperHero.text.toString().toInt() < 0
            ) {
                binding.combat.error = "Value needs to be between 0-100"
                isItInTheLimits = false
            }
        }

        return isItInTheLimits
    }

    fun editTextsAreEmpty(): Boolean {
        var editTextEmpty = true
        if (binding.nameOfSuperhero.text.toString().equals("")) {
            editTextEmpty = false
        }

        if (binding.descriptonOfSuperHero.text.toString().equals("")) {
            editTextEmpty = false
        }

        if (binding.heightOfSuperHero.text.toString().equals("")) {
            editTextEmpty = false
        }

        if (binding.weightOfSuperHero.text.toString().equals("")) {
            editTextEmpty = false
        }

        if (binding.intelligenceOfSuperHero.text.toString().equals("")) {
            editTextEmpty = false
        }

        if (binding.strengthOfSuperHero.text.toString().equals("")) {
            editTextEmpty = false
        }

        if (binding.speedOfSuperHero.text.toString().equals("")) {
            editTextEmpty = false
        }

        if (binding.durabilityOfSuperHero.text.toString().equals("")) {
            editTextEmpty = false
        }

        if (binding.combatOfSuperHero.text.toString().equals("")) {
            editTextEmpty = false
        }

        return editTextEmpty
    }
}