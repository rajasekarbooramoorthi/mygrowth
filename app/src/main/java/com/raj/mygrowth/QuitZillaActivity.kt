package com.raj.mygrowth

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.raj.mygrowth.adapter.ViewPagerAdapter
import com.raj.mygrowth.databinding.ActitvityQuitZillaBinding
import com.raj.mygrowth.domain.RequestAction
import com.raj.mygrowth.repository.Repository
import com.raj.mygrowth.uiState.UiState
import com.raj.mygrowth.viewModel.CommonViewModel
import kotlinx.coroutines.launch

class QuitZillaActivity : AppCompatActivity() {

    private lateinit var binding: ActitvityQuitZillaBinding

    private val context = this@QuitZillaActivity
    private val viewModel: CommonViewModel by viewModels {
        CommonViewModel.CommonViewModelFactory(Repository(this))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarInsetsActivity()

        binding = ActitvityQuitZillaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        callApi()

        binding.viewPager.adapter = ViewPagerAdapter(this)

        val adapter = ViewPagerAdapter(this)

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab, position ->
            tab.text = adapter.tabTitles [position]
        }.attach()
    }

    private fun callApi() {
        viewModel.fetchSprint(RequestAction("get_sprint_master"))
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