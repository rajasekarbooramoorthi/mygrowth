package com.raj.mygrowth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.raj.mygrowth.adapter.QuitZillaBenefitsAdapter
import com.raj.mygrowth.databinding.FragmentBenifitsBinding
import com.raj.mygrowth.repository.Repository
import com.raj.mygrowth.uiState.UiState
import com.raj.mygrowth.viewModel.CommonViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class FragmentBenefits : Fragment() {
    private var _binding: FragmentBenifitsBinding? = null
    private val binding get() = _binding!!

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val viewModel: CommonViewModel by viewModels {
        CommonViewModel.CommonViewModelFactory(
            Repository(requireContext())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBenifitsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        callApi()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun callApi() {
        viewModel.fetchQuitZillaBenefits()
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {

                    is UiState.Loading -> {
                    }

                    is UiState.SuccessQuitZillaBenefits -> {
                        val list = state.data.data.toMutableList()
                        list.shuffle()
                        val adapter = QuitZillaBenefitsAdapter(list, requireContext())
                        binding.recyclerViewVertical.adapter = adapter
                    }

                    is UiState.Error -> {
                    }

                    else -> {}
                }
            }
        }
    }
}