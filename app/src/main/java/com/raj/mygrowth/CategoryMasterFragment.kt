package com.raj.mygrowth

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.raj.mygrowth.adapter.MasterItemAdapter
import com.raj.mygrowth.adapter.MyCategoryMainAdapter
import com.raj.mygrowth.adapter.MySubCategoryAdapter
import com.raj.mygrowth.databinding.CategoryMainAdapterBinding
import com.raj.mygrowth.databinding.CategoryMasterFragmentBinding
import com.raj.mygrowth.databinding.DialogItemAdapterBinding
import com.raj.mygrowth.databinding.MasterItemAdapterBinding
import com.raj.mygrowth.databinding.SubCategoryAdpterBinding
import com.raj.mygrowth.domain.Category
import com.raj.mygrowth.domain.CategoryMasterResponse
import com.raj.mygrowth.domain.Item
import com.raj.mygrowth.domain.RequestAction
import com.raj.mygrowth.domain.SubCategory
import com.raj.mygrowth.interfaces.MasterInterFace
import com.raj.mygrowth.networkUtility.ApiService
import com.raj.mygrowth.networkUtility.RetrofitClient
import kotlinx.coroutines.launch
import java.net.URLEncoder
import androidx.core.net.toUri


class CategoryMasterFragment : MasterInterFace, Fragment() {

    private var _binding: CategoryMasterFragmentBinding? = null
    val bindingParent get() = _binding!!

    private val DOMAIN = "http://v8m.b07.mytemp.website/app/apps/"
    private var pathCurrent = ""
    private var pathType = ""

    private val api by lazy {
        RetrofitClient.instance.create(ApiService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = CategoryMasterFragmentBinding.inflate(inflater, container, false)
        return bindingParent.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingParent.recyclerViewHorizontalCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        bindingParent.recyclerViewSubCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        bindingParent.recyclerViewVerticalItem.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        loadMasterSkillLocal()
    }

    private fun showLoading() {
        bindingParent.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        bindingParent.progressBar.visibility = View.GONE
    }

    private fun loadMasterSkillLocal() {
        val mainList = Gson().fromJson(
            loadJSONFromAssets(),
            CategoryMasterResponse::class.java
        )
        bindingParent.recyclerViewHorizontalCategory.adapter =
            MyCategoryMainAdapter(
                mainList.data,
                this@CategoryMasterFragment
            )
    }

    private fun setupSearch(adapter: MasterItemAdapter) {
        bindingParent.searchView.visibility = View.VISIBLE
        bindingParent.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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


    override fun loadUrl(url: String, path: String) {
        val finalUrl = if (url.contains(".pdf")) {
            buildSafeUrl(DOMAIN, path, url)
        } else {
            ensureHttp(url)
        }
        openBrowser(finalUrl)
    }

    fun clickUrl(url: String) {
        val finalUrl = if (pathType == "pdf") {
            buildSafeUrl(DOMAIN, pathCurrent, url)
        } else {
            ensureHttp(url)
        }
        openBrowser(finalUrl)
    }

    private fun openBrowser(url: String) {
        val fixed = ensureHttp(url)
        startActivity(Intent(Intent.ACTION_VIEW, fixed.toUri()))
    }

    private fun ensureHttp(url: String): String {
        return if (url.startsWith("http://") || url.startsWith("https://")) url else "http://$url"
    }

    fun buildSafeUrl(domain: String, path: String, fileName: String): String {
        val encodedFileName =
            URLEncoder.encode(fileName, "UTF-8").replace("+", "%20").replace("%28", "(")
                .replace("%29", ")").replace("%2C", ",").replace("%26", "&").replace("%27", "'")

        return domain.trimEnd('/') + "/" + path.trim('/') + "/" + encodedFileName
    }

// ---------------- Callbacks ----------------

    fun click(id: String, path: String, type: String) {
        pathCurrent = path
        pathType = type
    }

    fun actionID(action: String, id: String, path: String) {
        loadDetails(action, id, path)
    }

    fun callApi(id: String) {
        // loadMaster(id)
    }

    fun clickChild(list: List<String>) {
        if (list.size == 1) clickUrl(list[0]) else dialog(list, "")
    }

    override fun callIntent(list: List<String>, folderName: String, fileType: String) {
        if (list.size == 1) loadUrl(list[0], folderName) else dialog(list, folderName)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


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


    fun loadJSONFromAssets(): String {
        return requireContext().assets.open("main.json").bufferedReader().use { it.readText() }
    }

    override fun clickSubCategory(list: List<SubCategory>) {
        this.bindingParent.recyclerViewSubCategory.adapter =
            MySubCategoryAdapter(list, this@CategoryMasterFragment)
    }

    override fun clickItem(list: List<Item>, folderName: String, fileType: String) {
        val adapter = MasterItemAdapter(
            list, this@CategoryMasterFragment, folderName, fileType
        )
        bindingParent.recyclerViewVerticalItem.adapter = adapter
        setupSearch(adapter)
    }


    private fun loadMasterSkill() {
        showLoading()
        bindingParent.searchView.visibility = View.GONE
        lifecycleScope.launch {
            runCatching {
                api.getMaster()
            }.onSuccess { response ->
                hideLoading()
                if (response.status) {
                    bindingParent.recyclerViewHorizontalCategory.adapter =
                        MyCategoryMainAdapter(response.data, this@CategoryMasterFragment)
                }
            }.onFailure {
                hideLoading()
                showError(it)
            }
        }
    }
}
