package com.raj.mygrowth

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.raj.mygrowth.databinding.FragmentAndroidInterviewBinding
import com.raj.mygrowth.domain.ConceptModel
import com.raj.mygrowth.domain.RequestAction
import com.raj.mygrowth.domain.RequestActionAndroidInterview
import com.raj.mygrowth.networkUtility.ApiService
import com.raj.mygrowth.networkUtility.RetrofitClient
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

class AndroidInterviewQuestionsFragment : Fragment() {

    private var _binding: FragmentAndroidInterviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAndroidInterviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadDailyTasks()
    }

    private fun loadDailyTasks() {

        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getAndroidInterview(RequestAction("get_interview_preparation"))

                binding.progressBar.visibility = View.GONE

                if (response.status) {
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    val adapter = AndroidInterviewQuestionAdapter(response.data)
                    binding.recyclerView.adapter = adapter
                    Toast.makeText(requireContext(), "Data Loaded", Toast.LENGTH_SHORT).show()
                }


            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }


        //pickCsvFile()

    }

    private fun sendToApi(list: ArrayList<ConceptModel>) {

        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val requestAction = RequestActionAndroidInterview("insert_interview_concept", list)
                val response = api.insertInterviewQuestion(requestAction)

                binding.progressBar.visibility = View.GONE

                if (response.status) {
                    Toast.makeText(requireContext(), "Data Loaded", Toast.LENGTH_SHORT).show()
                }


            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // avoid memory leak
    }

    private val csvPicker = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { readCsvFile(it) }
    }

    fun pickCsvFile() {
        csvPicker.launch("text/*")
    }

    private fun readCsvFile(uri: Uri) {
        try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val reader = BufferedReader(InputStreamReader(inputStream))

            val list = ArrayList<ConceptModel>()
            var isHeader = true

            reader.forEachLine { rawLine ->
                val line = rawLine.trim()
                if (line.isEmpty()) return@forEachLine   // skip blank rows

                if (isHeader) {
                    isHeader = false
                    return@forEachLine
                }

                // Split CSV safely, including text containing commas inside quotes
                val columns = mutableListOf<String>()
                var current = StringBuilder()
                var insideQuotes = false

                for (char in line) {
                    when (char) {
                        '"' -> insideQuotes = !insideQuotes
                        ',' -> {
                            if (insideQuotes) {
                                current.append(char)
                            } else {
                                columns.add(current.toString())
                                current = StringBuilder()
                            }
                        }

                        else -> current.append(char)
                    }
                }
                columns.add(current.toString()) // last value

                if (columns.size < 2) return@forEachLine

                val id = columns[0].trim()
                val name = columns[1].trim()
                val links = columns.drop(2).map { it.trim() }.filter { it.isNotEmpty() }

                list.add(ConceptModel(conceptId = id, conceptName = name, links = links))
            }

            reader.close()
            Log.d("CSV_FINAL", list.toString())

            // Send to API

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error reading CSV: ${e.message}", Toast.LENGTH_LONG)
                .show()
            Log.e("CSV_ERROR", e.toString())
        }
    }


}
