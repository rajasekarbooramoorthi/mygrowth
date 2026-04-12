package com.raj.mygrowth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.raj.mygrowth.adapter.DialogAdapterMasterAdapter
import com.raj.mygrowth.adapter.MasterItemAdapter
import com.raj.mygrowth.adapter.MyCategoryMainAdapter
import com.raj.mygrowth.adapter.MySubCategoryAdapter
import com.raj.mygrowth.databinding.CategoryMasterFragmentBinding
import com.raj.mygrowth.domain.CategoryMasterResponse
import com.raj.mygrowth.domain.Item
import com.raj.mygrowth.domain.SubCategory
import com.raj.mygrowth.interfaces.MasterInterFace
import com.raj.mygrowth.networkUtility.ApiService
import com.raj.mygrowth.networkUtility.RetrofitClient
import com.raj.mygrowth.networkUtility.RetrofitClient.DOMAIN
import kotlinx.coroutines.launch
import java.net.URLEncoder


class CategoryMasterFragment : MasterInterFace, Fragment() {

    private var _binding: CategoryMasterFragmentBinding? = null
    val bindingParent get() = _binding!!

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
        setRecyclerView()
        loadMasterSkillLocal()
    }

    private fun showLoading() {
        bindingParent.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        bindingParent.progressBar.visibility = View.GONE
    }

    private fun setRecyclerView() {

        bindingParent.recyclerViewHorizontalCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        bindingParent.recyclerViewSubCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)

        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.divider_recycler)
        drawable?.let {
            divider.setDrawable(it)
        }

        bindingParent.recyclerViewSubCategory.addItemDecoration(divider)

        bindingParent.recyclerViewVerticalItem.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    }

    private fun loadMasterSkillLocal() {
        val mainList = Gson().fromJson(
            loadJSONFromAssets(), CategoryMasterResponse::class.java
        )
        bindingParent.recyclerViewHorizontalCategory.adapter = MyCategoryMainAdapter(
            mainList.data, this@CategoryMasterFragment
        )
        mainList.data.firstOrNull()?.subcategoryList?.let { clickSubCategory(it) }

        val adapter = MasterItemAdapter(
            mainList.data.first().subcategoryList.first().itemList,
            this@CategoryMasterFragment,
            mainList.data.first().subcategoryList.first().folderName ?: "",
            mainList.data.first().subcategoryList.first().filetype ?: ""
        )
        bindingParent.recyclerViewVerticalItem.adapter = adapter
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
        rvDialog?.adapter = DialogAdapterMasterAdapter(list, this@CategoryMasterFragment, path)

        dialog.show()
    }


    fun loadJSONFromAssets(): String {
        return requireContext().assets.open("main.json").bufferedReader().use { it.readText() }
    }

    override fun clickSubCategory(list: List<SubCategory>) {
        this.bindingParent.recyclerViewSubCategory.adapter =
            MySubCategoryAdapter(list, this@CategoryMasterFragment)

        val adapter = MasterItemAdapter(
            listOf(), this@CategoryMasterFragment, "", ""
        )
        bindingParent.recyclerViewVerticalItem.adapter = adapter
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
