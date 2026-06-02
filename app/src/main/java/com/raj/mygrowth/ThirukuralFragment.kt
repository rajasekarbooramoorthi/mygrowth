package com.raj.mygrowth

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.raj.mygrowth.databinding.ThirukuralAdapterBinding
import com.raj.mygrowth.databinding.ThirukuralFragmentBinding
import com.raj.mygrowth.domain.ThirukuralResponseLatest
import com.raj.mygrowth.domain.kural
import com.raj.mygrowth.networkUtility.ApiService
import com.raj.mygrowth.networkUtility.RetrofitClient
import com.raj.mygrowth.repository.Repository
import com.raj.mygrowth.uiState.UiState
import com.raj.mygrowth.viewModel.CommonViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.getValue

class ThirukuralFragment : Fragment() {

    private var _binding: ThirukuralFragmentBinding? = null
    private val binding get() = _binding!!
    private val api by lazy { RetrofitClient.instance.create(ApiService::class.java) }
    private val gson by lazy { Gson() }

    private var fullList: List<kural> = emptyList()
    private var currentIndex = 0
    private val pageSize = 20
    private var isLoading = false
    private val viewModel: CommonViewModel by viewModels {
        CommonViewModel.CommonViewModelFactory(
            Repository(requireContext())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = ThirukuralFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //setupRecyclerView()
        callApi()
    }

    private fun setupRecyclerView_() {
        binding.recyclerViewVertical.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)

            val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            ContextCompat.getDrawable(requireContext(), R.drawable.divider_recycler)
                ?.let { divider.setDrawable(it) }

            addItemDecoration(divider)
        }
    }

    private fun setupRecyclerView() {

        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(requireContext(), R.drawable.divider_recycler)
            ?.let { divider.setDrawable(it) }

        binding.recyclerViewVertical.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            itemAnimator = null   // 🚀 removes lag
            addItemDecoration(divider)
        }
    }


    private fun showError(e: Throwable) {
        Toast.makeText(requireContext(), e.message ?: "Something went wrong", Toast.LENGTH_LONG)
            .show()
        Log.e("SinglePageMaster", "Error", e)
    }

    private fun loadData() {
        //showLoading()
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                fullList = withContext(Dispatchers.IO) {
                    requireContext().assets
                        .open("thirukural.json")
                        .bufferedReader()
                        .use { reader ->
                            gson.fromJson(
                                reader,
                                ThirukuralResponseLatest::class.java
                            )?.kural.orEmpty()
                        }
                }

                val adapter = KuralAdapter()

                binding.recyclerViewVertical.apply {
                    layoutManager = LinearLayoutManager(context)
                    itemAnimator = null
                    this.adapter = adapter
                }

                // Load first page
                loadNextPage(adapter)

                // Scroll listener
                setupPagination(adapter)

                setupSearch(adapter)

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
            } finally {
                hideLoading()
            }
        }
    }

    private fun loadNextPage(adapter: KuralAdapter) {
        if (isLoading) return

        isLoading = true

        val nextIndex = (currentIndex + pageSize).coerceAtMost(fullList.size)
        val subList = fullList.subList(currentIndex, nextIndex)

        adapter.addData(subList)
        //println("Scroll---> valll-->" + "fullList:" + fullList.size + ":subList" + subList.size)

        currentIndex = nextIndex
        isLoading = false
    }

    private fun setupPagination(adapter: KuralAdapter) {
        val layoutManager = binding.recyclerViewVertical.layoutManager as LinearLayoutManager

        binding.recyclerViewVertical.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && lastVisibleItem >= totalItemCount - 5) {
                    loadNextPage(adapter)
                }
                //println("Scroll--->" + "lastVisibleItem:" + lastVisibleItem + ":totalItemCount" + totalItemCount)
            }
        })
    }

    // ---------------- UI Helpers ----------------

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun callApi() {
        viewModel.fetchThirukural()
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        // show loader
                    }

                    is UiState.SuccessThirukural -> {

                        fullList = state.data.kural
                        val adapter = KuralAdapter()

                        binding.recyclerViewVertical.apply {
                            layoutManager = LinearLayoutManager(context)
                            itemAnimator = null
                            this.adapter = adapter
                        }

                        // Load first page
                        loadNextPage(adapter)

                        // Scroll listener
                        setupPagination(adapter)

                        setupSearch(adapter)
                    }

                    is UiState.Error -> {
                        // show error
                    }

                    else -> {

                    }
                }
            }
        }
    }


    class KuralAdapter(
        private val originalList: List<kural> = listOf()
    ) : RecyclerView.Adapter<KuralAdapter.ViewHolder>(), Filterable {

        private var filteredList = originalList.toMutableList()

        class ViewHolder(val binding: ThirukuralAdapterBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ThirukuralAdapterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ViewHolder(binding)
        }

        @SuppressLint("NotifyDataSetChanged")
        fun addData(list: List<kural>) {
            filteredList.addAll(list)
            notifyDataSetChanged()
        }

        override fun getItemCount() = filteredList.size

        override fun onBindViewHolder(
            holder: ViewHolder, @SuppressLint("RecyclerView") position: Int
        ) {
            val item = filteredList[position]
            bind(holder.binding, item)
        }

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val query = constraint?.toString()?.lowercase()?.trim() ?: ""

                    val result = if (query.isEmpty()) {
                        originalList
                    } else {
                        originalList.filter {
                            it.Line1.lowercase().contains(query) ||
                                    it.Line2.lowercase().contains(query)
                        }
                    }

                    return FilterResults().apply {
                        values = result
                    }
                }

                @Suppress("UNCHECKED_CAST")
                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    filteredList.clear()
                    filteredList.addAll(results?.values as List<kural>)
                    notifyDataSetChanged()
                }
            }
        }

        private fun bind(binding: ThirukuralAdapterBinding, item: kural) {
            binding.apply {
                // tvAdhikaram.text = "${item.kural_no}.${item.pal_tamil} "
                // tvIyal.text = item.iyal_tamil
                tvKuralTamil.text = buildString {
                    append(item.Line1)
                    append("\n")
                    append(item.Line2)
                }
                tvExplanationTamil.text = item.mk
                tvExplanationEnglish.text = item.explanation
            }
        }
    }

    private fun setupSearch(adapter: KuralAdapter) {
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

}