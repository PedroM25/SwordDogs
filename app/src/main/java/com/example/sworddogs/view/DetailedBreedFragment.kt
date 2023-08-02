package com.example.sworddogs.view

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.example.sworddogs.BUNDLE_BREED_GROUP
import com.example.sworddogs.BUNDLE_BREED_NAME
import com.example.sworddogs.BUNDLE_BREED_ORIGIN
import com.example.sworddogs.BUNDLE_BREED_TEMPERAMENT
import com.example.sworddogs.R
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

        binding.breedName.text =
            arguments?.getCharSequence(BUNDLE_BREED_NAME) ?: getString(R.string.detailed_breed_not_found)

        binding.breedOrigin.text =
            Html.fromHtml(getString(R.string.detailed_breed_origin, arguments?.getCharSequence(
                BUNDLE_BREED_ORIGIN)
                ?: getString(R.string.detailed_breed_not_found)),HtmlCompat.FROM_HTML_MODE_COMPACT)

        binding.breedTemperament.text =
            Html.fromHtml(getString(R.string.detailed_breed_temperament, arguments?.getCharSequence(
                BUNDLE_BREED_TEMPERAMENT)
                ?: getString(R.string.detailed_breed_not_found)),HtmlCompat.FROM_HTML_MODE_COMPACT)

        binding.breedGroup.text =
            Html.fromHtml(getString(R.string.detailed_breed_group, arguments?.getCharSequence(
                BUNDLE_BREED_GROUP)
                ?: getString(R.string.detailed_breed_not_found)),HtmlCompat.FROM_HTML_MODE_COMPACT)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}