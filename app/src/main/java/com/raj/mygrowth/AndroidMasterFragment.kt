package com.raj.mygrowth

import android.content.Intent
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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.raj.mygrowth.databinding.FragmentAndroidMasterBinding
import com.raj.mygrowth.domain.ConceptModel
import com.raj.mygrowth.domain.RequestAction
import com.raj.mygrowth.domain.RequestActionAndroidInterview
import com.raj.mygrowth.interfaces.SimpleClick
import com.raj.mygrowth.networkUtility.ApiService
import com.raj.mygrowth.networkUtility.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URLEncoder


class AndroidMasterFragment : Fragment(), SimpleClick {

    private var _binding: FragmentAndroidMasterBinding? = null
    private val binding get() = _binding!!
    private var PATH_CURRENT = ""
    private var PATH_TYPE = ""
    private var DOMANI = "http://v8m.b07.mytemp.website/app/apps/"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAndroidMasterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadMaster()
    }

    private fun loadMaster() {

        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getAndroidMaster(RequestAction("get_master_skills"))

                binding.progressBar.visibility = View.GONE

                if (response.status) {
                    binding.recyclerViewHorizontal.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    val adapter =
                        MasterSkillAdapter(
                            response.data,
                            this@AndroidMasterFragment
                        )
                    binding.recyclerViewHorizontal.adapter = adapter
                    binding.recyclerViewHorizontal.isNestedScrollingEnabled = false
                }


            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadDetails(tag: String) {

        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getAndroidMasterData(RequestAction(tag))

                binding.progressBar.visibility = View.GONE

                if (response.status) {
                    binding.recyclerViewVertical.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

                    val adapter =
                        MasterGenericAdapter(
                            response.data,
                            this@AndroidMasterFragment
                        )
                    binding.recyclerViewVertical.adapter = adapter
                    binding.recyclerViewVertical.isNestedScrollingEnabled = false
                }


            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun sendToApi(list: ArrayList<ConceptModel>) {

        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val requestAction =
                    RequestActionAndroidInterview("insert_android_skill_specific", list)
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

        println("aj uri-->$uri")
        lifecycleScope.launch(Dispatchers.IO) {

            val list = ArrayList<ConceptModel>()

            try {
                val inputStream = requireContext().contentResolver.openInputStream(uri)
                val reader = BufferedReader(InputStreamReader(inputStream))

                var isHeader = true

                reader.forEachLine { rawLine ->
                    val line = rawLine.trim()
                    if (line.isEmpty()) return@forEachLine // skip blank rows

                    if (isHeader) {
                        isHeader = false
                        return@forEachLine
                    }

                    // Split CSV safely including commas inside quotes
                    val columns = mutableListOf<String>()
                    var current = StringBuilder()
                    var insideQuotes = false

                    for (char in line) {
                        when (char) {
                            '"' -> insideQuotes = !insideQuotes
                            ',' -> {
                                if (insideQuotes) current.append(char)
                                else {
                                    columns.add(current.toString())
                                    current = StringBuilder()
                                }
                            }

                            else -> current.append(char)
                        }
                    }
                    columns.add(current.toString())

                    if (columns.isEmpty()) return@forEachLine

                    val name = columns[0].trim()

                    // ❗ Skip if name is empty
                    if (name.isEmpty()) return@forEachLine

                    val links = columns.drop(1).map { it.trim() }.filter { it.isNotEmpty() }
                    val id = (list.size + 1).toString()

                    list.add(
                        ConceptModel(
                            conceptId = id,
                            conceptName = name,
                            links = links
                        )
                    )
                }

                reader.close()

                withContext(Dispatchers.Main) {
                    Log.d("CSV_FINAL", list.firstOrNull().toString())
                    //sendToApi(list)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Error reading CSV: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("CSV_ERROR", e.toString())
                }
            }
        }
    }

    override fun click(id: String, path: String, type: String) {
        PATH_CURRENT = path
        PATH_TYPE = type
        loadDetails(id)
        //pickCsvFile()
    }

    override fun clickChild(list: List<String>) {
        if (list.size == 1) {
            clickUrl(list[0])
        } else {
            dialog(list)
        }
    }

    override fun clickUrl(url: String) {
        if (PATH_TYPE == "pdf") {
            val prep = buildSafeUrl(DOMANI, PATH_CURRENT, url)
            val intent = Intent(context, ActivityPdfView::class.java)
            intent.putExtra("FILE_URL", prep)
            startActivity(intent)
            println("Url pdf-->$prep")
        } else {
            val intent = Intent(context, ActivityWebView::class.java)
            intent.putExtra("FILE_URL", url)
            startActivity(intent)
            println("Url pdf-->$url")

        }
    }

    fun buildSafeUrl(domain: String, path: String, fileName: String): String {
        // Encode only file name, not full URL
        val encodedFileName = URLEncoder.encode(fileName, "UTF-8")
            .replace("+", "%20")        // proper space
            .replace("%28", "(")        // keep brackets readable
            .replace("%29", ")")
            .replace("%2C", ",")        // keep comma
            .replace("%26", "&")        // keep ampersand if needed in name
            .replace("%27", "'")

        return domain.trimEnd('/') + "/" +
                path.trim('/') + "/" +
                encodedFileName
    }

    fun dialog(list: List<String>) {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetTheme)
        dialog.setContentView(R.layout.adapterdialog)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        val rvDialog = dialog.findViewById<RecyclerView>(R.id.rvDialog)
        rvDialog?.layoutManager = LinearLayoutManager(requireContext())
        rvDialog?.adapter = DialogAdapterGenericAdapter(list, this@AndroidMasterFragment)
        rvDialog?.isNestedScrollingEnabled = false

        dialog.show()
    }

}
