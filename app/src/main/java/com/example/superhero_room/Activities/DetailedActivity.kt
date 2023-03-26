package com.example.superhero_room.Activities

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.superhero_room.Adapter.SuperHeroAdapter
import com.example.superhero_room.R
import com.example.superhero_room.Room.SuperheroEntity
import com.example.superhero_room.ViewModels.DetailedActivityViewModel
import com.example.superhero_room.databinding.ActivityDetailedBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*


class DetailedActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailedBinding
    lateinit var viewModel: DetailedActivityViewModel
    private var coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    var superheroEntity: SuperheroEntity? = null
    var barEntry: ArrayList<BarEntry> = ArrayList()
    var names: ArrayList<String> = ArrayList()
    lateinit var name: String
    lateinit var superheroBitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[DetailedActivityViewModel::class.java]
        setSupportActionBar(binding.toolbar)
        name = intent.getStringExtra("name").toString()
        binding.collapsingToolBar.title = name
        binding.progressBar.visibility = View.VISIBLE
        coroutineScope.launch {
            val loadingJob = launch {
                superheroEntity = viewModel.getDetails(name)
                val file = File("data/data/com.example.superhero_room/files/$name.jpg")
                superheroBitmap = BitmapFactory.decodeFile(file.absolutePath)
            }

            loadingJob.join()
            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.INVISIBLE
                binding.descriptionBody.text = superheroEntity?.description
                binding.height.text = superheroEntity?.height
                binding.weight.text = superheroEntity?.weight
                binding.superheroImage.setImageBitmap(superheroBitmap)
                setupBarChart()
            }
        }

        toastsOfPowerImages()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.example.superhero_room.R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.deleteSuperHero -> MaterialAlertDialogBuilder(this)
                .setTitle("Are you sure you want to delete it")
                .setMessage("This action cannot be undone !!!")
                .setPositiveButton("", DialogInterface.OnClickListener { dialog, which ->
                    coroutineScope.launch {
                        viewModel.deleteSuperHero(superheroEntity?.name!!)

                        startActivity(Intent(this@DetailedActivity, MainActivity::class.java))
                    }
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->

                })
                .setPositiveButtonIcon(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.deletesuperhero
                    )
                )
                .create()
                .show()

        }
        return true
    }


    private fun toastsOfPowerImages() {
        binding.intelligenceImage.setOnClickListener {
            Toast.makeText(this, "Intelligence", Toast.LENGTH_SHORT).show()
        }

        binding.speedImage.setOnClickListener {
            Toast.makeText(this, "Speed", Toast.LENGTH_SHORT).show()
        }

        binding.strengthImage.setOnClickListener {
            Toast.makeText(this, "Strength", Toast.LENGTH_SHORT).show()
        }

        binding.durabilityImage.setOnClickListener {
            Toast.makeText(this, "Durability", Toast.LENGTH_SHORT).show()
        }

        binding.combatImage.setOnClickListener {
            Toast.makeText(this, "Combat", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBarChart() {
        barEntry.add(BarEntry(10f, superheroEntity?.intelligence!!))
        barEntry.add(BarEntry(20f, superheroEntity?.strength!!))
        barEntry.add(BarEntry(30f, superheroEntity?.speed!!))
        barEntry.add(BarEntry(40f, superheroEntity?.durability!!))
        barEntry.add(BarEntry(50f, superheroEntity?.combat!!))

        /* var xAxis: XAxis = binding.barChart.xAxis
       xAxis.valueFormatter = IndexAxisValueFormatter(names)
       xAxis.position = XAxis.XAxisPosition.TOP
       xAxis.setDrawAxisLine(true)
       xAxis.granularity = 100f
       xAxis.textColor = Color.WHITE
       xAxis.textSize = 30f
       xAxis.mLabelHeight = 100
       xAxis.labelCount = names.size
       xAxis.setDrawLabels(true)
       xAxis.mLabelWidth = 30
       xAxis.position = XAxis.XAxisPosition.BOTTOM;
       xAxis.setDrawLabels(true);*/

        val chart = binding.barChart

        chart.setTouchEnabled(false)
        chart.description.isEnabled = false
        chart.legend.isEnabled = false
        chart.axisLeft.isEnabled = true
        chart.axisRight.isEnabled = true
        chart.axisLeft.axisMinimum = 0F
        chart.axisRight.axisMinimum = 0F
        chart.xAxis.setDrawAxisLine(false)
        chart.xAxis.setDrawGridLines(false)
        chart.xAxis.granularity = 1f
        binding.barChart.setFitBars(true)
        //   chart.xAxis.valueFormatter = IndexAxisValueFormatter(names)
        chart.xAxis.valueFormatter = object : IndexAxisValueFormatter(names) {
            override fun getFormattedValue(value: Float, axis: AxisBase): String {
                return super.getFormattedValue(value - 0.5f, axis)
            }
        }

        chart.xAxis.textColor =
            ContextCompat.getColor(this, com.example.superhero_room.R.color.white)
        chart.xAxis.textSize = 12f


        /* val yAxisLeft: YAxis = binding.barChart.getAxisLeft()
         yAxisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
         yAxisLeft.setDrawGridLines(false)
         yAxisLeft.setDrawAxisLine(false)
         yAxisLeft.isEnabled = false*/

        // binding.barChart.getAxisRight().setEnabled(false)


        var barDataSet: BarDataSet = BarDataSet(barEntry, "Strength Set")
        barDataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        barDataSet.valueTextColor = Color.WHITE
        var barData: BarData = BarData(barDataSet)
        barData.barWidth = 5f
        binding.barChart.data = barData
        binding.barChart.description.isEnabled = false

        binding.barChart.animateY(1500, Easing.EaseInOutCubic)
        binding.barChart.invalidate()

    }
}