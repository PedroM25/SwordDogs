package com.example.sworddogs.view.breeds

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sworddogs.LOAD_THRESHOLD
import com.example.sworddogs.R
import com.example.sworddogs.SPAN
import com.example.sworddogs.databinding.FragmentBreedsBinding
import com.example.sworddogs.viewmodel.BreedsViewModel

class BreedsFragment : Fragment() {

    private var _binding: FragmentBreedsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val breedsAdapter = BreedsAdapter()
    private lateinit var breedsViewModel: BreedsViewModel

    private var isFirstApiCall = true
    private var allBreedsLoaded = false
    private var currentSpan = 1

    //state seems to be lost when switch between apps of the bottom navigation bar?
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        breedsViewModel =
            ViewModelProvider(this).get(BreedsViewModel::class.java)
        _binding = FragmentBreedsBinding.inflate(inflater, container, false)

        //adapter stuff
        binding.allBreeds.adapter = breedsAdapter

        subscribe()
        //listen for state changes
        if (isFirstApiCall) {
            // it's either this + live datas
            // or
            // make API calls directly in the Adapter to be able to directly manipulate
            // list of breeds
            isFirstApiCall = false
            breedsViewModel.getBreedsData(LOAD_THRESHOLD)
            Log.i("PEDRO", "Fetched $LOAD_THRESHOLD breeds initially")
        }

        //RecyclerView stuff
        val gridLayoutManager = GridLayoutManager(requireContext(), currentSpan)
        binding.allBreeds.layoutManager = gridLayoutManager
        binding.allBreeds.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    Log.i("PEDRO", "Scrolled to bottom?")
                    if (!allBreedsLoaded) {
                        breedsViewModel.getBreedsData(LOAD_THRESHOLD)
                    }
                } else if (!recyclerView.canScrollVertically(-1) && dy < 0) {
                    Log.i("PEDRO", "Scrolled to top")
                }
            }
        })

        //button stuff
        if (currentSpan > 1){
            binding.gridLinearSwitchButton.setIconResource(R.drawable.baseline_view_list_24)
        }
        if (currentSpan == 1){
            binding.gridLinearSwitchButton.setIconResource(R.drawable.baseline_grid_view_24)
        }

        binding.gridLinearSwitchButton.setOnClickListener {
            val grid = binding.allBreeds.layoutManager as GridLayoutManager
            if (grid.spanCount > 1){
                binding.gridLinearSwitchButton.setIconResource(R.drawable.baseline_grid_view_24)
                currentSpan = 1
                grid.spanCount = currentSpan
                return@setOnClickListener
            }
            if (grid.spanCount == 1){
                binding.gridLinearSwitchButton.setIconResource(R.drawable.baseline_view_list_24)
                currentSpan = SPAN
                grid.spanCount = currentSpan
                return@setOnClickListener
            }
        }

        binding.orderAlphabeticallyButton.setOnClickListener {
            breedsAdapter.orderAlphabetically()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribe() {
        breedsViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading)
                Log.i("BreedsFragment", "Loading data from API...")
        }

        breedsViewModel.isDone.observe(viewLifecycleOwner) { isDone ->
            if (isDone) {
                Log.i("BreedsFragment", "API depleted, no more breeds to fetch")
                allBreedsLoaded = true
            }
        }

        breedsViewModel.isError.observe(viewLifecycleOwner) { isError ->
            if (isError) {
                Log.i("BreedsFragment", breedsViewModel.errorMessage)

                val toast = Toast.makeText(requireContext(), breedsViewModel.errorMessage, Toast.LENGTH_SHORT) // in Activity
                toast.show()
            }
        }

        /* ANTIGO
        breedsViewModel.allBreedsData.observe(viewLifecycleOwner) { allBreedsData ->
            val breedsAdapter = BreedsAdapter(allBreedsData)
            binding.allBreeds.adapter = breedsAdapter
        }
        */

        breedsViewModel.limitedBreedsData.observe(viewLifecycleOwner) { limitedBreedsData ->
            Log.i("PEDRO", "the breeds list changed! new breeds received: $limitedBreedsData ")
            breedsAdapter.addReceivedBreeds(limitedBreedsData)
        }

        /*
        breedsViewModel.isGridLayout.observe(viewLifecycleOwner) { isGridLayout ->
            // maybe here change the adapter
        }
         */
    }
}