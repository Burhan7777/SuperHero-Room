package com.example.superhero_room.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.superhero_room.Activities.DetailedActivity
import com.example.superhero_room.R

class SuperHeroAdapter(
    superheroNames: ArrayList<String>,
    superHeroImages: ArrayList<Drawable?>,
    context: Context
) :
    RecyclerView.Adapter<SuperHeroAdapter.ViewHolder>() {

    private var superHeroImages: ArrayList<Drawable?>
    private var superheroNames: ArrayList<String>
    private var context: Context

    init {
        this.superHeroImages = superHeroImages
        this.superheroNames = superheroNames
        this.context = context
    }

    class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        var textView: TextView = view.findViewById(R.id.mainTextView)
        var imageView: ImageView = view.findViewById(R.id.mainImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_row_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = superheroNames[position]
        holder.imageView.setImageDrawable(superHeroImages[position])
        holder.imageView.setOnClickListener {
            var intent = Intent(context, DetailedActivity::class.java)
            intent.putExtra("name", superheroNames[position])
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return superHeroImages.size
    }

}