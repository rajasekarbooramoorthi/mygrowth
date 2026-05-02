package com.raj.mygrowth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.raj.mygrowth.databinding.FragmentDietBinding
import com.raj.mygrowth.domain.DietResponse
import com.raj.mygrowth.repository.Repository
import com.raj.mygrowth.uiState.UiState
import com.raj.mygrowth.viewModel.CommonViewModel
import kotlinx.coroutines.launch

class DietFragment : Fragment() {
    private var _binding: FragmentDietBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CommonViewModel by viewModels {
        CommonViewModel.CommonViewModelFactory(Repository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDietBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        callApi()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun callApi() {
        viewModel.fetchDietPlan()
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        // show loader
                    }

                    is UiState.SuccessDiet -> {

                        val adapter = DietAdapter()
                        adapter.submitData(state.data)
                        binding.recyclerView.adapter = adapter
                    }

                    is UiState.Error -> {
                        // show error
                    }

                    else -> {

                    }
                }
            }
        }
    }
}


