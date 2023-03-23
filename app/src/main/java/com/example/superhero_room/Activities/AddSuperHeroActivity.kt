package com.example.superhero_room.Activities

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.superhero_room.databinding.ActivityAddSuperHeroBinding
import com.github.drjacky.imagepicker.ImagePicker.Companion.with
import com.github.drjacky.imagepicker.constant.ImageProvider

class AddSuperHeroActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddSuperHeroBinding


    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val uri = it.data?.data!!
                Glide.with(this@AddSuperHeroActivity).load(uri).fitCenter().circleCrop()
                    .into(binding.superheroUploadImage)

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSuperHeroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.superHeroPhotoSelectionButton.setOnClickListener {
            with(this@AddSuperHeroActivity)
                .crop()
                .cropOval()
                .maxResultSize(512, 512, true)
                .galleryOnly()
                .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                .createIntentFromDialog { launcher.launch(it) }
        }
    }
}