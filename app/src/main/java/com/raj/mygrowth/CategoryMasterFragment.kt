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
import android.widget.Filter.FilterResults
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
import com.raj.mygrowth.databinding.AdapterItemMyGrowthActionviewBinding
import com.raj.mygrowth.databinding.CategoryMainAdapterBinding
import com.raj.mygrowth.databinding.FragmentAndroidMasterBinding
import com.raj.mygrowth.databinding.MasterItemAdapterBinding
import com.raj.mygrowth.databinding.SubCategoryAdpterBinding
import com.raj.mygrowth.domain.Category
import com.raj.mygrowth.domain.CategoryMasterResponse
import com.raj.mygrowth.domain.Item
import com.raj.mygrowth.domain.RequestAction
import com.raj.mygrowth.domain.ResponseGenericItem
import com.raj.mygrowth.domain.SubCategory
import com.raj.mygrowth.interfaces.MasterInterFace
import com.raj.mygrowth.interfaces.SimpleClick
import com.raj.mygrowth.networkUtility.ApiService
import com.raj.mygrowth.networkUtility.RetrofitClient
import kotlinx.coroutines.launch
import java.net.URLEncoder


class CategoryMasterFragment : MasterInterFace, Fragment() {

    private var _binding: FragmentAndroidMasterBinding? = null
    val bindingParent get() = _binding!!

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
        return bindingParent.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingParent.recyclerViewHorizontalSkill.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        bindingParent.recyclerViewHorizontal.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        bindingParent.recyclerViewVertical.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        loadMasterSkill()
    }

    private fun showLoading() {
        bindingParent.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        bindingParent.progressBar.visibility = View.GONE
    }

    private fun loadMasterSkill() {

        val json = loadJSONFromAssets()

        val gson = Gson()
        val mainList = gson.fromJson(json, CategoryMasterResponse::class.java)

        mainList.data.forEach {
            println("PrintData" + it.categoryName + " -> " + it.categoryId)
        }
        bindingParent.recyclerViewHorizontalSkill.adapter =
            MyCategoryMainAdapter(mainList.data, this@CategoryMasterFragment)

    }


    private fun loadDetails(action: String, id: String, path: String) {
        showLoading()

        lifecycleScope.launch {
            runCatching {
                api.getAndroidMasterData(RequestAction(action, id))
            }.onSuccess { response ->
                hideLoading()
                if (response.status) {
                    /* val adapter = MygrowthitemactionClickAdapter(
                         response.data,
                         this@CategoryMasterFragment,
                         path
                     )
                     bindingParent.recyclerViewVertical.adapter = adapter
                     bindingParent.recyclerViewVertical.visibility = View.VISIBLE
                     setupSearch(adapter)*/
                }
            }.onFailure {
                hideLoading()
                showError(it)
            }
        }
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

// ---------------- URL Handling ----------------

    fun loadUrl(url: String, path: String) {
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

    fun callIntent(list: List<String>, path: String) {
        if (list.size == 1) loadUrl(list[0], path) else dialog(list, path)
    }

    fun checkCompleted(id: String) {}

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
        //   rvDialog?.adapter = DialogAdapterGenericAdapter(list, this, path)

        dialog.show()
    }


    fun loadJSONFromAssets(): String {
        return requireContext().assets.open("main.json")
            .bufferedReader()
            .use { it.readText() }
    }

    override fun clickSubCategory(list: List<SubCategory>) {
        this.bindingParent.recyclerViewHorizontal.adapter =
            MySubCategoryAdapter(list, this@CategoryMasterFragment)
    }

    override fun clickItem(list: List<Item>) {
        val adapter = MasterItemAdapter(
            list,
            this@CategoryMasterFragment,
            ""
        )
        bindingParent.recyclerViewVertical.adapter = adapter
        setupSearch(adapter)
    }


    class MyCategoryMainAdapter(
        private val list: List<Category>, click_: MasterInterFace
    ) : RecyclerView.Adapter<MyCategoryMainAdapter.ViewHolder>() {
        var click = click_

        private var selectedPosition = RecyclerView.NO_POSITION

        class ViewHolder(val binding: CategoryMainAdapterBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = CategoryMainAdapterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ViewHolder(binding)
        }

        override fun getItemCount() = list.size

        override fun onBindViewHolder(
            holder: ViewHolder,
            @SuppressLint("RecyclerView") position: Int
        ) {
            val item = list[position]
            if (position == selectedPosition) {
                holder.binding.linearName.setBackgroundResource(R.drawable.bg_item_selected)
            } else {
                holder.binding.linearName.setBackgroundResource(R.drawable.bg_item_normal)
            }
            holder.binding.tvName.text = item.categoryName

            holder.binding.linearName.setOnClickListener {

                val previous = selectedPosition
                selectedPosition = position

                if (previous != RecyclerView.NO_POSITION) {
                    notifyItemChanged(previous)
                }
                notifyItemChanged(selectedPosition)
                click.clickSubCategory(item.subcategoryList)
            }
            holder.binding.executePendingBindings()
        }
    }


    class MySubCategoryAdapter(
        private val list: List<SubCategory>, click_: MasterInterFace
    ) : RecyclerView.Adapter<MySubCategoryAdapter.ViewHolder>() {
        var click = click_
        private var selectedPosition = RecyclerView.NO_POSITION

        class ViewHolder(val binding: SubCategoryAdpterBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = SubCategoryAdpterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ViewHolder(binding)
        }

        override fun getItemCount() = list.size

        override fun onBindViewHolder(
            holder: ViewHolder,
            @SuppressLint("RecyclerView") position: Int
        ) {
            val item = list[position]
            if (position == selectedPosition) {
                holder.binding.linearName.setBackgroundResource(R.drawable.bg_item_selected)
            } else {
                holder.binding.linearName.setBackgroundResource(R.drawable.bg_child_item_normal)
            }
            holder.binding.tvName.text = item.subcategoryName
            holder.binding.root.setOnClickListener {
                val previous = selectedPosition
                selectedPosition = position
                if (previous != RecyclerView.NO_POSITION) {
                    notifyItemChanged(previous)
                }
                notifyItemChanged(selectedPosition)
                click.clickItem(item.itemList)
            }
            holder.binding.executePendingBindings()
        }
    }


    class MasterItemAdapter(
        private val originalList: List<Item>,
        click_: MasterInterFace,
        path_: String
    ) : RecyclerView.Adapter<MasterItemAdapter.ViewHolder>(), Filterable {

        private var filteredList = originalList.toMutableList()

        var click = click_
        var path = path_

        class ViewHolder(val binding: MasterItemAdapterBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = MasterItemAdapterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ViewHolder(binding)
        }

        override fun getItemCount() = filteredList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = filteredList[position]

            //holder.binding.tvName.text = item.name
            holder.binding.tvName.text =
                HtmlCompat.fromHtml(
                    item.id + "\t\t" + item.name,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )

            holder.binding.root.setOnClickListener {
                // item.links?.let { click.callIntent(it, path) }
            }

            holder.binding.executePendingBindings()
        }

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val query = constraint?.toString()?.lowercase()?.trim() ?: ""

                    val result = if (query.isEmpty()) {
                        originalList
                    } else {
                        originalList.filter {
                            it.name.lowercase().contains(query)
                            //|| it.id.contains(query)
                        }
                    }

                    return FilterResults().apply {
                        values = result
                    }
                }

                @Suppress("UNCHECKED_CAST")
                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    filteredList.clear()
                    filteredList.addAll(results?.values as List<Item>)
                    notifyDataSetChanged()
                }
            }
        }
    }

}
