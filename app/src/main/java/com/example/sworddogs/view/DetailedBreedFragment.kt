package com.example.sworddogs.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sworddogs.databinding.FragmentDetailedBreedBinding
import com.example.sworddogs.viewmodel.DetailedBreedViewModel

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
        val detailedBreedViewModel =
            ViewModelProvider(this).get(DetailedBreedViewModel::class.java)

        _binding = FragmentDetailedBreedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDetailedBreed
        detailedBreedViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}