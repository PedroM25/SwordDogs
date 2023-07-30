package com.example.sworddogs.view.breeds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sworddogs.ListAllBreeds
import com.example.sworddogs.R

class BreedsAdapter(private val listOfAllBreeds: ListAllBreeds) :
    RecyclerView.Adapter<BreedsAdapter.BreedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_breed, parent, false)
        return BreedViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BreedViewHolder, position: Int) {
        val data = listOfAllBreeds[position]
        holder.breedNameTextView.text = data.name
        Glide
            .with(holder.dogBreedImageView)
            .load(data.image?.url)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.dogBreedImageView)
    }

    override fun getItemCount() = listOfAllBreeds.size

    class BreedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dogBreedImageView: ImageView = itemView.findViewById(R.id.image_dog_breed)
        val breedNameTextView: TextView = itemView.findViewById(R.id.breed_name)
        // You can also set click listeners or other customization here if needed
    }
}