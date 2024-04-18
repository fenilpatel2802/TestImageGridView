package com.wipro.testimagescrollapp.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wipro.shopease.utils.Resource
import com.wipro.testimagescrollapp.R
import com.wipro.testimagescrollapp.adapter.ImageViewRecyclerAdapter
import com.wipro.testimagescrollapp.databinding.ActivityMainBinding
import com.wipro.testimagescrollapp.ui.BaseActivity

class MainActivity : BaseActivity() {

    // binding
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    // view model
    private val viewModel: MainViewModel by viewModels()

    // adapter
    private lateinit var mImageAdapter: ImageViewRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initImageRecyclerAdapter()
        initObserver()
    }

    private fun initObserver() {
        viewModel.response.observe(this) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data?.let { productList ->
                        if (productList.isNotEmpty()) {
                            viewModel.productList.addAll(productList)
                            mImageAdapter.notifyDataSetChanged()
                            binding.tvNoData.visibility = View.GONE
                        } else {
                            viewModel.isLastPage = true
                            if (viewModel.page == 1) {
                                binding.tvNoData.visibility = View.VISIBLE
                            }
                        }
                    }
                    // loader
                    binding.progressBar.visibility = View.GONE
                    binding.progressBarFooter.visibility = View.GONE
                }

                Resource.Status.ERROR -> {
                    viewModel.isLastPage = true
                    if (viewModel.page == 1) {
                        binding.tvNoData.visibility = View.VISIBLE
                    }

                    // loader
                    binding.progressBar.visibility = View.GONE
                    binding.progressBarFooter.visibility = View.GONE
                }

                Resource.Status.LOADING -> {
                    if (viewModel.page == 1) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBarFooter.visibility = View.VISIBLE
                    }
                    binding.tvNoData.visibility = View.GONE
                }
            }
        }
    }

    private fun initImageRecyclerAdapter() {
        mImageAdapter = ImageViewRecyclerAdapter(this, viewModel.productList)
        val mLayoutManager = GridLayoutManager(this@MainActivity, 2)
        with(binding) {
            rvImage.apply {
                this.adapter = mImageAdapter
                this.layoutManager = mLayoutManager
            }
            rvImage.setItemViewCacheSize(500)
            rvImage.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = mLayoutManager.childCount
                    val totalItemCount = mLayoutManager.itemCount
                    val firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition()


                    if (!viewModel.isLoading && !viewModel.isLastPage) {
                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                            && firstVisibleItemPosition >= 0 && dy >= 0
                        ) {
                            viewModel.page++
                            viewModel.callImageApi()
                        }
                    }
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}