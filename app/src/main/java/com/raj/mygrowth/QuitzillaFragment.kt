package com.raj.mygrowth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.raj.mygrowth.adapter.ViewPagerAdapter
import com.raj.mygrowth.databinding.FragmentQuitZillaBinding
import com.raj.mygrowth.domain.RequestAction
import com.raj.mygrowth.repository.Repository
import com.raj.mygrowth.uiState.UiState
import com.raj.mygrowth.viewModel.CommonViewModel
import kotlinx.coroutines.launch

class QuitzillaFragment : Fragment() {

    private var _binding: FragmentQuitZillaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CommonViewModel by viewModels {
        CommonViewModel.CommonViewModelFactory(
            Repository(requireContext())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuitZillaBinding.inflate(inflater, container, false)

        val adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab, position ->
            tab.text = adapter.tabTitles[position]
        }.attach()

        callApi()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun callApi() {
        viewModel.fetchSprint(RequestAction("get_quit_zilla_master"))
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {

                    is UiState.Loading -> {
                        // show loader
                    }

                    is UiState.SuccessSprintMaster -> {
                        val data = state.data
                    }

                    is UiState.Error -> {
                        // show error
                    }

                    else -> {}
                }
            }
        }
    }

}

