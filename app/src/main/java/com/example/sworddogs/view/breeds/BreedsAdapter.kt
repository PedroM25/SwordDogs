package com.example.sworddogs.view.breeds

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sworddogs.BUNDLE_BREED_GROUP
import com.example.sworddogs.BUNDLE_BREED_NAME
import com.example.sworddogs.BUNDLE_BREED_ORIGIN
import com.example.sworddogs.BUNDLE_BREED_TEMPERAMENT
import com.example.sworddogs.ListOfBreeds
import com.example.sworddogs.R
import com.example.sworddogs.model.BreedResponse

class BreedsAdapter(private var listOfLimitedBreeds: MutableList<BreedResponse> = mutableListOf()) :
    RecyclerView.Adapter<BreedsAdapter.BreedViewHolder>() {

    private val CLASS_TAG get() = this::class.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_photo_breed_card, parent, false)
        return BreedViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BreedViewHolder, position: Int) {
        val data = listOfLimitedBreeds[position]
        holder.itemView.setOnClickListener{
            val args = Bundle()
            args.putCharSequence(BUNDLE_BREED_NAME, data.name)
            args.putCharSequence(BUNDLE_BREED_ORIGIN, data.origin)
            args.putCharSequence(BUNDLE_BREED_TEMPERAMENT, data.temperament)
            args.putCharSequence(BUNDLE_BREED_GROUP, data.breedGroup)
            it.findNavController().navigate(R.id.action_navigation_breeds_to_navigation_detailed_breed, args)
        }

        holder.breedNameTextView.text = data.name
        Glide
            .with(holder.dogBreedImageView)
            .load(data.image?.url)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.dogBreedImageView)
    }

    override fun getItemCount() = listOfLimitedBreeds.size

    fun addReceivedBreeds(receivedBreeds : ListOfBreeds){
        Log.d(CLASS_TAG, "addAll new breeds + notifyDataSetChanged()")
        listOfLimitedBreeds.addAll(receivedBreeds)
        notifyDataSetChanged()
    }

    fun orderAlphabetically(){
        Log.i(CLASS_TAG, "Ordering alphabetically existing breeds ")
        listOfLimitedBreeds = listOfLimitedBreeds.sortedWith(compareBy { it.name }).toMutableList()
        notifyDataSetChanged()
    }

    class BreedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dogBreedImageView: ImageView = itemView.findViewById(R.id.image_dog_breed)
        val breedNameTextView: TextView = itemView.findViewById(R.id.breed_name)
    }
}