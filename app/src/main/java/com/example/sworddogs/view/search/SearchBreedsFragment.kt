package com.example.sworddogs.view.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sworddogs.ListOfBreeds
import com.example.sworddogs.databinding.FragmentSearchBreedsBinding
import com.example.sworddogs.viewmodel.SearchViewModel
import java.util.LinkedList

class SearchBreedsFragment : Fragment() {

    private var _binding: FragmentSearchBreedsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var searchViewModel: SearchViewModel

    private var firstOnCreateView = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchViewModel =
            ViewModelProvider(this).get(SearchViewModel::class.java)

        _binding = FragmentSearchBreedsBinding.inflate(inflater, container, false)
        if (firstOnCreateView){
            subscribe()
            firstOnCreateView = false
        }

        val allBreeds: ListOfBreeds = LinkedList()
        //SearchView stuff
        val searchView: SearchView = binding.simpleSearchView
        searchView.setOnSearchClickListener {
            // add loading spinner
            // fetch all breeds
                // when user hits "Search", if allBreeds.isEmpty() perform API call
            // perform search of user input against allBreeds vector
            // choose relevant items to display (simple "is substring of names of breeds")
            // remove spinner
            // show results
        }

        return binding.root
    }

    private fun subscribe() {
        searchViewModel.isLoading.observe(requireActivity()) { isLoading ->
            if (isLoading)
                Log.i("SearchBreedsFragment", "Loading data from API and organizing relevant results...")
        }

        searchViewModel.isError.observe(requireActivity()) { isError ->
            if (isError) {
                Log.i("SearchBreedsFragment", searchViewModel.errorMessage)

                val toast = Toast.makeText(requireContext(), searchViewModel.errorMessage, Toast.LENGTH_SHORT) // in Activity
                toast.show()
            }
        }

       searchViewModel.relevantBreedsFromSearchInput.observe(viewLifecycleOwner) { allBreedsData ->
            val breedsAdapter = SearchBreedsAdapter(allBreedsData)
            binding.searchBreedsRecyclerView.adapter = breedsAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
