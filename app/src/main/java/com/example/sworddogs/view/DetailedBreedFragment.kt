package com.example.sworddogs.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sworddogs.databinding.FragmentDetailedBreedBinding

class DetailedBreedFragment : Fragment() {

    private var _binding: FragmentDetailedBreedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailedBreedBinding.inflate(inflater, container, false)
        binding.breedName.text = arguments?.getCharSequence("breedName")
        binding.breedOrigin.text = arguments?.getCharSequence("breedOrigin")
        binding.breedTemperament.text = arguments?.getCharSequence("breedTemperament")
        binding.breedGroup.text = arguments?.getCharSequence("breedGroup")

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}