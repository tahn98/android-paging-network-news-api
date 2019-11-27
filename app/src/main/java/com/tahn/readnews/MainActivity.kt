package com.tahn.readnews

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tahn.readnews.adapter.NewsAdapter
import com.tahn.readnews.adapter.OnItemClickListener
import com.tahn.readnews.adapter.ToolbarAdapter
import com.tahn.readnews.data.Article
import com.tahn.readnews.data.ToolbarTitle
import com.tahn.readnews.viewmodel.NewsViewModel
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var toolbarList : RecyclerView
    private lateinit var newsList: RecyclerView
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var search: AppCompatImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        bindControls()
        bindEvents()
        newsViewModel = viewModel()

        initAdapterToolbar()
        initNewsAdapter()
        bindViewModel()
    }

    private fun bindViewModel(){
        newsViewModel.showResultsByCategory("business")
    }

    private fun initAdapterToolbar(){
        val listTitle = listOf(ToolbarTitle("Business"), ToolbarTitle("Entertainment"), ToolbarTitle("General"), ToolbarTitle("Health"), ToolbarTitle("Science"))
        val adapterToolbar = ToolbarAdapter(listTitle, object : OnItemClickListener{
            override fun onClick(title: ToolbarTitle) {
                newsViewModel.showResultsByCategory(title.title.toLowerCase(Locale.getDefault()))
            }

        })
        toolbarList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        toolbarList.adapter = adapterToolbar
    }

    private fun viewModel(): NewsViewModel {
        val viewModelFactory = Injection.provideViewModelFactory()
        return ViewModelProviders.of(this, viewModelFactory)[NewsViewModel::class.java]
    }

    private fun initNewsAdapter() {
        val adapter = NewsAdapter() {
            newsViewModel.retry()
        }
        newsList.adapter = adapter

        newsViewModel.listArticles.observe(this, Observer<PagedList<Article>> {
            adapter.submitList(null)
            adapter.submitList(it)
        })
        newsViewModel.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })
    }

    private fun bindControls() {
        newsList = findViewById(R.id.list)
        toolbarList = findViewById(R.id.main_toolbar)
        search = findViewById(R.id.navigation_search)
    }

    private fun bindEvents() {
        search.setOnClickListener {
            startActivitySearch()
        }
    }

    private fun startActivitySearch(){
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}
