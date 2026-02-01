package com.raj.mygrowth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.raj.mygrowth.databinding.FragmentAndroidMasterBinding
import com.raj.mygrowth.domain.RequestAction
import com.raj.mygrowth.interfaces.SimpleClick
import com.raj.mygrowth.networkUtility.ApiService
import com.raj.mygrowth.networkUtility.RetrofitClient
import kotlinx.coroutines.launch
import java.net.URLEncoder


class SinglePageMasterFragment : Fragment(), SimpleClick {

    private var _binding: FragmentAndroidMasterBinding? = null
    private val binding get() = _binding!!

    private val DOMAIN = "http://v8m.b07.mytemp.website/app/apps/"
    private var pathCurrent = ""
    private var pathType = ""

    private val api by lazy {
        RetrofitClient.instance.create(ApiService::class.java)
    }

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

        binding.recyclerViewHorizontalSkill.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.recyclerViewHorizontal.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.recyclerViewVertical.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        loadMasterSkill()
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun loadMasterSkill() {
        showLoading()
        binding.searchView.visibility = View.GONE
        binding.recyclerViewVertical.visibility = View.GONE

        lifecycleScope.launch {
            runCatching {
                api.getMyGrowth(RequestAction("gey_mygrowth"))
            }.onSuccess { response ->
                hideLoading()
                if (response.status) {
                    binding.recyclerViewHorizontalSkill.adapter =
                        Mygrowthdapter(response.data, this@SinglePageMasterFragment)
                }
            }.onFailure {
                hideLoading()
                showError(it)
            }
        }
    }

    private fun loadMaster(tag: String) {
        showLoading()
        binding.recyclerViewVertical.visibility = View.GONE
        binding.searchView.visibility = View.GONE

        lifecycleScope.launch {
            runCatching {
                api.getMyGrowth(RequestAction(tag))
            }.onSuccess { response ->
                hideLoading()
                if (response.status) {
                    binding.recyclerViewHorizontal.adapter =
                        Mygrowthdapterchild(response.data, this@SinglePageMasterFragment)
                }
            }.onFailure {
                hideLoading()
                showError(it)
            }
        }
    }

    private fun loadDetails(action: String, id: String, path: String) {
        showLoading()

        lifecycleScope.launch {
            runCatching {
                api.getAndroidMasterData(RequestAction(action, id))
            }.onSuccess { response ->
                hideLoading()
                if (response.status) {
                    val adapter = MygrowthitemactionClickAdapter(
                        response.data,
                        this@SinglePageMasterFragment,
                        path
                    )
                    binding.recyclerViewVertical.adapter = adapter
                    binding.recyclerViewVertical.visibility = View.VISIBLE
                    setupSearch(adapter)
                }
            }.onFailure {
                hideLoading()
                showError(it)
            }
        }
    }

    private fun setupSearch(adapter: MygrowthitemactionClickAdapter) {
        binding.searchView.visibility = View.VISIBLE
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
    }

    private fun showError(e: Throwable) {
        Toast.makeText(requireContext(), e.message ?: "Something went wrong", Toast.LENGTH_LONG)
            .show()
        Log.e("SinglePageMaster", "Error", e)
    }

    // ---------------- URL Handling ----------------

    override fun loadUrl(url: String, path: String) {
        val finalUrl = if (url.contains(".pdf")) {
            buildSafeUrl(DOMAIN, path, url)
        } else {
            ensureHttp(url)
        }
        openBrowser(finalUrl)
    }

    override fun clickUrl(url: String) {
        val finalUrl = if (pathType == "pdf") {
            buildSafeUrl(DOMAIN, pathCurrent, url)
        } else {
            ensureHttp(url)
        }
        openBrowser(finalUrl)
    }

    private fun openBrowser(url: String) {
        val fixed = ensureHttp(url)
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(fixed)))
    }

    private fun ensureHttp(url: String): String {
        return if (url.startsWith("http://") || url.startsWith("https://")) url else "http://$url"
    }

    fun buildSafeUrl(domain: String, path: String, fileName: String): String {
        val encodedFileName = URLEncoder.encode(fileName, "UTF-8")
            .replace("+", "%20")
            .replace("%28", "(")
            .replace("%29", ")")
            .replace("%2C", ",")
            .replace("%26", "&")
            .replace("%27", "'")

        return domain.trimEnd('/') + "/" + path.trim('/') + "/" + encodedFileName
    }

    // ---------------- Callbacks ----------------

    override fun click(id: String, path: String, type: String) {
        pathCurrent = path
        pathType = type
    }

    override fun actionID(action: String, id: String, path: String) {
        loadDetails(action, id, path)
    }

    override fun callApi(id: String) {
        loadMaster(id)
    }

    override fun clickChild(list: List<String>) {
        if (list.size == 1) clickUrl(list[0]) else dialog(list, "")
    }

    override fun callIntent(list: List<String>, path: String) {
        if (list.size == 1) loadUrl(list[0], path) else dialog(list, path)
    }

    override fun checkCompleted(id: String) {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // ---------------- Bottom Sheet ----------------

    fun dialog(list: List<String>, path: String) {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetTheme)
        dialog.setContentView(R.layout.adapterdialog)
        dialog.setCancelable(true)

        dialog.setOnShowListener {
            val bottomSheet =
                (it as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                BottomSheetBehavior.from(sheet).apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    skipCollapsed = true
                }
                sheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            }
        }

        val rvDialog = dialog.findViewById<RecyclerView>(R.id.rvDialog)
        rvDialog?.layoutManager = LinearLayoutManager(requireContext())
        rvDialog?.adapter = DialogAdapterGenericAdapter(list, this, path)

        dialog.show()
    }
}
