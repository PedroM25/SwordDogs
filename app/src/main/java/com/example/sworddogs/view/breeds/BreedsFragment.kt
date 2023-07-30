package com.example.sworddogs.view.breeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.sworddogs.databinding.FragmentBreedsBinding
import com.example.sworddogs.viewmodel.BreedsViewModel

class BreedsFragment : Fragment() {

    private var _binding: FragmentBreedsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var breedsViewModel: BreedsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        breedsViewModel =
            ViewModelProvider(this).get(BreedsViewModel::class.java)
        subscribe()
        breedsViewModel.getAllBreedsData()

        _binding = FragmentBreedsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribe() {
        breedsViewModel.isLoading.observe(viewLifecycleOwner) {  isLoading ->
            // Is sending the API request
        }

        breedsViewModel.isError.observe(viewLifecycleOwner) { isError ->
            // Encountered an error in the process
        }

        breedsViewModel.allBreedsData.observe(viewLifecycleOwner) { allBreedsData ->
            val breedsAdapter = BreedsAdapter(allBreedsData)
            binding.allBreeds.adapter = breedsAdapter
        }
    }
}