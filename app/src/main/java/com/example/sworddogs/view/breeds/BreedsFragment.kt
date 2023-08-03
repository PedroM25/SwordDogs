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

    private var didFirstDownload = false
    private var allBreedsLoaded = false
    private var currentSpan = 1

    private val CLASS_TAG get() = this::class.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(CLASS_TAG, "onCreateView entered")
        breedsViewModel =
            ViewModelProvider(this).get(BreedsViewModel::class.java)
        _binding = FragmentBreedsBinding.inflate(inflater, container, false)

        //adapter stuff
        binding.allBreeds.adapter = breedsAdapter

        // it's either this + live datas
        // or
        // make API calls directly in the Adapter to be able to directly manipulate
        // list of breeds
        subscribe()
        if (!didFirstDownload) {
            Log.i("PEDRO", "Didn't do first download so, attempt ")
            breedsViewModel.getBreedsData(LOAD_THRESHOLD)
            Log.i(CLASS_TAG, "Fetched $LOAD_THRESHOLD breeds initially")
        }

        //RecyclerView stuff
        val gridLayoutManager = GridLayoutManager(requireContext(), currentSpan)
        binding.allBreeds.layoutManager = gridLayoutManager
        binding.allBreeds.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    Log.i(CLASS_TAG, "Scrolled to bottom")
                    if (!allBreedsLoaded) {
                        breedsViewModel.getBreedsData(LOAD_THRESHOLD)
                    }
                } else if (!recyclerView.canScrollVertically(-1) && dy < 0) {
                    Log.i(CLASS_TAG, "Scrolled to top")
                }
            }
        })

        // "Grid/linear switch" button stuff
        if (currentSpan > 1)
            binding.gridLinearSwitchButton.setIconResource(R.drawable.baseline_view_list_24)
        if (currentSpan == 1)
            binding.gridLinearSwitchButton.setIconResource(R.drawable.baseline_grid_view_24)
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

        // "Order alphabetically" button stuff
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
            Log.d(CLASS_TAG, "LIVE DATA isLoading entered")
            if (isLoading) {
                Log.i(CLASS_TAG, "Loading data from API...")
                binding.statusMessage.text = getString(R.string.loading)
                binding.statusMessage.visibility = View.VISIBLE
            }
        }

        breedsViewModel.isDone.observe(viewLifecycleOwner) { isDone ->
            Log.d(CLASS_TAG, "LIVE DATA isDone entered")
            if (isDone) {
                Log.i(CLASS_TAG, "API depleted, no more breeds to fetch")
                allBreedsLoaded = true
            }
        }

        breedsViewModel.isError.observe(viewLifecycleOwner) { isError ->
            Log.d(CLASS_TAG, "LIVE DATA isError entered")
            if (isError) {
                if (!didFirstDownload) {
                    binding.statusMessage.text = breedsViewModel.errorMessage
                    binding.statusMessage.visibility = View.VISIBLE
                } else {
                    val toast = Toast.makeText(requireContext(), breedsViewModel.errorMessage, Toast.LENGTH_SHORT)
                    toast.show()
                }

                // Dev notes: Also considered and also seems feasible but I prefer the text option

            }
        }

        breedsViewModel.limitedBreedsData.observe(viewLifecycleOwner) { limitedBreedsData ->
            if (!didFirstDownload) {
                Log.i("PEDRO", "After doing first download: didFirstDownload now TRUE")
                didFirstDownload = true
            }
            Log.d(CLASS_TAG, "LIVE DATA limitedBreedsData entered")
            Log.d(CLASS_TAG, "New set of breeds received: $limitedBreedsData")
            binding.statusMessage.visibility = View.GONE
            breedsAdapter.addReceivedBreeds(limitedBreedsData)
        }
    }
}