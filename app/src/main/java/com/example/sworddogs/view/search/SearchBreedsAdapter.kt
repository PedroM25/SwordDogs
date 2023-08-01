package com.example.sworddogs.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sworddogs.R
import com.example.sworddogs.model.BreedResponse

class SearchBreedsAdapter (private var listOfSearchRelevantBreeds: List<BreedResponse> = listOf()) :
RecyclerView.Adapter<SearchBreedsAdapter.SearchBreedsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchBreedsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_search_breed_card, parent, false)
        return SearchBreedsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SearchBreedsViewHolder, position: Int) {
        val data = listOfSearchRelevantBreeds[position]
        holder.itemView.setOnClickListener{
            val args = Bundle()
            args.putCharSequence("breedName", data.name)
            args.putCharSequence("breedOrigin", data.origin)
            args.putCharSequence("breedTemperament", data.temperament)
            args.putCharSequence("breedGroup", data.breedGroup)
            it.findNavController().navigate(R.id.action_navigation_search_to_navigation_detailed_breed, args)
        }

        holder.breedNameTextView.text = data.name
        holder.breedGroupTextView.text = data.breedGroup
        holder.breedOriginTextView.text = data.origin
    }

    override fun getItemCount() = listOfSearchRelevantBreeds.size

    class SearchBreedsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val breedNameTextView: TextView = itemView.findViewById(R.id.breed_name)
        val breedGroupTextView: TextView = itemView.findViewById(R.id.breed_group)
        val breedOriginTextView: TextView = itemView.findViewById(R.id.breed_origin)
        // You can also set click listeners or other customization here if needed
    }
}