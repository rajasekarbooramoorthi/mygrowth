package com.raj.mygrowth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.raj.mygrowth.adapter.QuitZillaReportAdapter
import com.raj.mygrowth.databinding.FragmentQuitZillaReportBinding
import com.raj.mygrowth.domain.RequestAction
import com.raj.mygrowth.repository.Repository
import com.raj.mygrowth.uiState.UiState
import com.raj.mygrowth.viewModel.CommonViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class FragmentRGUpdate : Fragment() {
    private var _binding: FragmentQuitZillaReportBinding? = null
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
        _binding = FragmentQuitZillaReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        callApi()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun callApi() {
        viewModel.fetchQuitZillaReport(RequestAction("get_quit_zilla_master"))
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {

                    is UiState.Loading -> {
                        // show loader
                        Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                    }

                    is UiState.SuccessQuitZillaReport -> {
                        val list = state.data.data
                        val adapter = QuitZillaReportAdapter(list, requireContext())
                        binding.recyclerViewVertical.adapter = adapter
                        Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show()
                    }

                    is UiState.Error -> {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }
        }
    }
}