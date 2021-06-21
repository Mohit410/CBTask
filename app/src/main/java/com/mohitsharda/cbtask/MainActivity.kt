package com.mohitsharda.cbtask

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohitsharda.cbtask.adapter.PostsPagingAdapter
import com.mohitsharda.cbtask.databinding.ActivityMainBinding
import com.mohitsharda.cbtask.viewModel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private val postViewModel: PostViewModel by viewModels()

    @Inject
    lateinit var postsPagingAdapter: PostsPagingAdapter

    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.postRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = postsPagingAdapter
        recyclerView.setHasFixedSize(true)

        lifecycleScope.launchWhenStarted {
            postViewModel.getAllPost().collectLatest {
                binding.apply {
                    recyclerView.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
                postsPagingAdapter.submitData(it)
            }
        }
    }
}