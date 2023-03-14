package com.example.superhero_room.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler
import androidx.lifecycle.ViewModelProvider
import com.example.superhero_room.R
import com.example.superhero_room.Room.SuperheroEntity
import com.example.superhero_room.ViewModels.DetailedActivityViewModel
import com.example.superhero_room.databinding.ActivityDetailedBinding
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.straiberry.android.charts.extenstions.toChartData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

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
        setSupportActionBar(binding.toolbar)

        coroutineScope.launch {
            superheroEntity = viewModel.getDetails("Superman")
            withContext(Dispatchers.Main) {
                // binding.description.text = superheroEntity?.description

            }
        }

        setupBarChart()

    }

    private fun setupBarChart() {
        val data: ArrayList<HashMap<String, Int?>> = arrayListOf()

        repeat(7) {
            data.add(hashMapOf(Pair("sunday", 10)))
        }

        binding.barChartViewBrushing.animate(data.toChartData())
    }
}