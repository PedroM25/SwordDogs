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
import com.example.sworddogs.databinding.FragmentSearchBreedsBinding
import com.example.sworddogs.viewmodel.SearchBreedsViewModel

class SearchBreedsFragment : Fragment() {

    private var _binding: FragmentSearchBreedsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var searchBreedsViewModel: SearchBreedsViewModel

    private var firstOnCreateView = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchBreedsViewModel =
            ViewModelProvider(this).get(SearchBreedsViewModel::class.java)

        _binding = FragmentSearchBreedsBinding.inflate(inflater, container, false)
        if (firstOnCreateView){
            subscribe()
            firstOnCreateView = false
        }

        //SearchView stuff
        val searchView: SearchView = binding.simpleSearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Handle the search query submission
                searchBreedsViewModel.getRelevantBreedsFromSearchInput(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Handle changes in the search query text
                // ignore for now
                return true
            }
        })

        // add loading spinner
        // fetch all breeds
            // when user hits "Search", if allBreeds.isEmpty() perform API call
        // perform search of user input against allBreeds vector
        // choose relevant items to display (simple "is substring of names of breeds")
        // remove spinner
        // show results

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun subscribe() {
        searchBreedsViewModel.isLoading.observe(requireActivity()) { isLoading ->
            if (isLoading)
                Log.i("SearchBreedsFragment", "Loading data from API and organizing relevant results...")
        }

        searchBreedsViewModel.isError.observe(requireActivity()) { isError ->
            if (isError) {
                Log.i("SearchBreedsFragment", searchBreedsViewModel.errorMessage)

                val toast = Toast.makeText(requireContext(), searchBreedsViewModel.errorMessage, Toast.LENGTH_SHORT) // in Activity
                toast.show()
            }
        }

       searchBreedsViewModel.relevantBreedsFromSearchInput.observe(requireActivity()) { allBreedsData ->
            val breedsAdapter = SearchBreedsAdapter(allBreedsData)
            binding.searchBreedsRecyclerView.adapter = breedsAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
