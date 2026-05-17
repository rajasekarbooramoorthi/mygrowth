package com.raj.mygrowth

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.raj.mygrowth.databinding.FragmentWeightGainReportBinding
import com.raj.mygrowth.databinding.FragmentWeightGainReportBinding.inflate
import com.raj.mygrowth.domain.WeightData
import com.raj.mygrowth.repository.Repository
import com.raj.mygrowth.viewModel.CommonViewModel

class WeightGainReportFragment : Fragment() {
    private var _binding: FragmentWeightGainReportBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CommonViewModel by viewModels {
        CommonViewModel.CommonViewModelFactory(Repository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupChart()
    }


    private fun setupChart() {

        val targetEntries = ArrayList<Entry>()
        val currentEntries = ArrayList<Entry>()
        val labels = ArrayList<String>()

        // =========================
        // TARGET WEIGHT
        // =========================

        val targetWeightList = listOf(

            // MAY
            48.400f,
            50.75f,
            51.5f,
            52.25f,

            // JUNE
            53f,
            53.75f,
            54.5f,
            55.25f,

            // JULY
            56f,
            56.75f,
            57.5f,
            58.25f,

            // AUGUST
            59f,
            59.75f,
            60.5f,
            61.25f,

            // SEPTEMBER
            62f,
            62.75f,
            63.5f,
            64.25f,

            // OCTOBER
            65f,
            65.75f,
            66.5f,
            67.25f
        )

        // =========================
        // CURRENT WEIGHT
        // ADD WEEKLY DATA HERE
        // =========================

        val currentWeightList = mutableListOf(
            48.400f,
            49f,
            50.400f,
            51f,

            51.400f,
        )

        // =========================
        // LABELS
        // =========================

        val weekLabels = listOf(

            "W1\nMay",
            "W2",
            "W3",
            "W4",

            "W1\nJun",
            "W2",
            "W3",
            "W4",

            "W1\nJul",
            "W2",
            "W3",
            "W4",

            "W1\nAug",
            "W2",
            "W3",
            "W4",

            "W1\nSep",
            "W2",
            "W3",
            "W4",

            "W1\nOct",
            "W2",
            "W3",
            "W4"
        )

        // =========================
        // TARGET ENTRIES
        // =========================

        for (i in targetWeightList.indices) {
            targetEntries.add(
                Entry(i.toFloat(), targetWeightList[i])
            )
            labels.add(weekLabels[i])
        }

        // =========================
        // CURRENT ENTRIES
        // ONLY AVAILABLE DATA
        // =========================

        for (i in currentWeightList.indices) {
            currentEntries.add(
                Entry(i.toFloat(), currentWeightList[i])
            )
        }

        // =========================
        // TARGET LINE
        // =========================

        val targetDataSet = LineDataSet(
            targetEntries,
            "Target Weight"
        ).apply {
            color = getColor(requireContext(), R.color.deep_orange_light)
            lineWidth = 3f
            setCircleColor(getColor(requireContext(), R.color.amber_dark_50))
            circleRadius = 4f
            setDrawValues(false)
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }

        // =========================
        // CURRENT LINE
        // =========================

        val currentDataSet = LineDataSet(
            currentEntries,
            "Current Weight"
        ).apply {
            color = getColor(
                requireContext(),
                R.color.teal_dark_50
            )
            lineWidth = 3f
            setCircleColor(
                getColor(
                    requireContext(),
                    R.color.teal_light
                )
            )
            circleRadius = 5f
            setDrawValues(true)
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }

        val lineData = LineData(
            targetDataSet,
            currentDataSet
        )

        // =========================
        // CHART UI
        // =========================

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
}