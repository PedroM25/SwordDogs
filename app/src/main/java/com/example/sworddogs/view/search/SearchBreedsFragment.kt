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
import com.example.sworddogs.R
import com.example.sworddogs.databinding.FragmentSearchBreedsBinding
import com.example.sworddogs.viewmodel.SearchBreedsViewModel

class SearchBreedsFragment : Fragment() {

    private var _binding: FragmentSearchBreedsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var searchBreedsViewModel: SearchBreedsViewModel

    private val CLASS_TAG get() = this::class.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(CLASS_TAG, "onCreateView entered")
        searchBreedsViewModel =
            ViewModelProvider(this).get(SearchBreedsViewModel::class.java)

        _binding = FragmentSearchBreedsBinding.inflate(inflater, container, false)
        subscribe()

        //SearchView stuff
        val searchView: SearchView = binding.simpleSearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.i(CLASS_TAG, "User searched for: \"$query\"")
                searchBreedsViewModel.getRelevantBreedsFromSearchInput(query)
                return true
            }

            // Dev notes: This alternative was considered but ultimately,
            // I found it too much
            override fun onQueryTextChange(newText: String): Boolean {
                Log.d(CLASS_TAG, "Search query text changed to \"$newText\"?")
                return true
            }
        })

        // "X button in SearchView" stuff
        val clearButton: View = searchView.findViewById(androidx.appcompat.R.id.search_close_btn)
        clearButton.setOnClickListener {
            searchView.setQuery("", false)
            binding.searchBreedsRecyclerView.adapter = null
            binding.statusMessage.visibility = View.VISIBLE
            binding.statusMessage.text = getString(R.string.fragment_search_breeds_nothing_to_show_yet)
        }

        return binding.root
    }

    private fun subscribe() {
        searchBreedsViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            Log.d(CLASS_TAG, "LIVE DATA isLoading entered")
            if (isLoading) {
                Log.i(CLASS_TAG,"Getting data from API + searching for user-input-relevant results...")
                binding.statusMessage.text = "Loading..."
                binding.statusMessage.visibility = View.VISIBLE
                //start spinner here
            }
        }

        searchBreedsViewModel.isError.observe(viewLifecycleOwner) { isError ->
            Log.d(CLASS_TAG, "LIVE DATA isError entered")
            if (isError) {
                binding.statusMessage.text = searchBreedsViewModel.errorMessage
                binding.statusMessage.visibility = View.VISIBLE

                // Dev notes: Also considered and also seems feasible but I prefer the text option
                //val toast = Toast.makeText(requireContext(), searchBreedsViewModel.errorMessage, Toast.LENGTH_SHORT)
                //toast.show()
            }
        }

       searchBreedsViewModel.relevantBreedsFromSearchInput.observe(viewLifecycleOwner) { allBreedsData ->
           Log.d(CLASS_TAG, "LIVE DATA relevantBreedsFromSearchInput entered")
           Log.d(CLASS_TAG, "live data: $allBreedsData ")
           binding.statusMessage.visibility = View.GONE
           val breedsAdapter = SearchBreedsAdapter(allBreedsData)
            binding.searchBreedsRecyclerView.adapter = breedsAdapter
           //stop spinner here?
       }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
