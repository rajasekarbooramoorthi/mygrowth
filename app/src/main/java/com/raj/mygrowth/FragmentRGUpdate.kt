package com.raj.mygrowth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.raj.mygrowth.databinding.FragmentQoutsBinding
import com.raj.mygrowth.databinding.FragmentRgUpdateBinding
import java.text.SimpleDateFormat
import java.util.Locale

class FragmentRGUpdate : Fragment() {
    private var _binding: FragmentRgUpdateBinding? = null
    private val binding get() = _binding!!

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRgUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}