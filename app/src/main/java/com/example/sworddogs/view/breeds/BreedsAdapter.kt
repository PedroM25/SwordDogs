package com.example.sworddogs.view.breeds

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.sworddogs.ListAllBreeds

class BreedsAdapter(private val listOfAllBreeds: ListAllBreeds) :
    RecyclerView.Adapter<BreedsAdapter.ViewHolder>() {



    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = listOfAllBreeds.size

    class BreedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val nameTextView: TextView = itemView.findViewById(R.id.titleTextView)
        // You can also set click listeners or other customization here if needed
    }
}