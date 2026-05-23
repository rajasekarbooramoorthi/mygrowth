package com.raj.mygrowth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.gson.Gson
import com.raj.mygrowth.databinding.FragmentWeightGainReportBinding
import com.raj.mygrowth.databinding.FragmentWeightGainReportBinding.inflate
import com.raj.mygrowth.domain.WeightGainResponse
import com.raj.mygrowth.repository.Repository
import com.raj.mygrowth.uiState.UiState
import com.raj.mygrowth.viewModel.CommonViewModel
import kotlinx.coroutines.launch

class WeightGainReportFragment : Fragment() {
    private var _binding: FragmentWeightGainReportBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CommonViewModel by viewModels {
        CommonViewModel.CommonViewModelFactory(Repository(requireContext()))
    }
    private val gson by lazy { Gson() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callApi()
    }

    private fun setupChart(dataResponse: WeightGainResponse) {

        val targetEntries = ArrayList<Entry>()
        val currentEntries = ArrayList<Entry>()
        val labels = ArrayList<String>()

        for (i in dataResponse.targetWeight.indices) {
            targetEntries.add(
                Entry(i.toFloat(), dataResponse.targetWeight[i].weight)
            )
            labels.add(dataResponse.targetWeight[i].week)
        }

        for (i in dataResponse.currentWeight.indices) {
            currentEntries.add(
                Entry(i.toFloat(), dataResponse.currentWeight[i].weight)
            )
        }

        val targetDataSet = LineDataSet(
            targetEntries, "Target Weight"
        ).apply {
            color = getColor(requireContext(), R.color.deep_orange_light_time_line)
            lineWidth = 3f
            setCircleColor(getColor(requireContext(), R.color.amber_dark_50))
            circleRadius = 4f
            setDrawValues(false)
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }

        val currentDataSet = LineDataSet(
            currentEntries, "Current Weight"
        ).apply {
            color = getColor(
                requireContext(), R.color.teal_dark_50
            )
            lineWidth = 3f
            setCircleColor(
                getColor(
                    requireContext(), R.color.teal_dark_light_time_line
                )
            )
            circleRadius = 5f
            setDrawValues(true)
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }

        val lineData = LineData(
            targetDataSet, currentDataSet
        )

        binding.lineChart.apply {

            data = lineData
            description.isEnabled = false
            animateX(2200)
            setTouchEnabled(true)
            setPinchZoom(true)
            legend.isEnabled = true
            axisRight.isEnabled = false
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                textSize = 10f
                labelRotationAngle = -45f
                valueFormatter = IndexAxisValueFormatter(labels)
                setDrawGridLines(false)
            }
            axisLeft.apply {
                textSize = 10f
            }
            invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadData(): String {
        return requireContext().assets.open("weightGainTimeline.json").bufferedReader()
            .use { it.readText() }
    }

    fun callApi() {
        viewModel.fetchWeightGainTimeLine()
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is UiState.Loading -> {}

                    is UiState.SuccessWeightGainTimeLine -> {
                        val data = state.data
                        setupChart(data)
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
