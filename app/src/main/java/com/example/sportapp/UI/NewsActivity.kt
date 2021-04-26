package com.example.sportapp.UI

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sportapp.Data.News
import com.example.sportapp.IUseBottomNav
import com.example.sportapp.UI.Adapter.NewsAdapter
import com.example.sportapp.databinding.ActivityNewsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class NewsActivity : AppCompatActivity(), IUseBottomNav {
    private lateinit var adapter: NewsAdapter
    private lateinit var binding: ActivityNewsBinding

    companion object {
        const val EXTRA_URL = "url"
        const val EXTRA_STATE = "state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActiveMenu(binding.bottomNavView.menu,0)
        setUpMenuItemListener(binding.bottomNavView,this,0)
        setUpAdapter()
        if (savedInstanceState==null){
            fetchNews()
        } else {
            val newsList = savedInstanceState.getParcelableArrayList<News>(EXTRA_STATE)
            newsList?.let { adapter.setNewsData(it) }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getNewsList())
    }

    private fun setUpAdapter(){
        adapter = NewsAdapter()
        adapter.notifyDataSetChanged()
        adapter.setOnItemClickCallback(object : NewsAdapter.OnItemClickCallback {
            override fun onItemClicked(data: News) {
                Toast.makeText(this@NewsActivity,data.title,Toast.LENGTH_SHORT).show()
                val intent = Intent(this@NewsActivity, NewsDetailActivity::class.java)
                intent.putExtra(EXTRA_URL, data.url)
                startActivity(intent)
            }
        })
        val orientation = resources.configuration.orientation
        val spanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 2 else 1
        binding.rvNews.layoutManager = GridLayoutManager(this, spanCount)
        binding.rvNews.adapter = adapter
    }

    private fun fetchNews() {
        val newsList = ArrayList<News>()
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        val url =
            "https://newsapi.org/v2/top-headlines?country=id&category=sports&apiKey=b1c5c123d872477c980adf41226b1d4a"
        showLoading(true)

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val articles = JSONObject(result).getJSONArray("articles")
                    for (idx in 0 until articles.length()) {
                        val data = articles.getJSONObject(idx)
                        val news = News(
                            title = data.getString("title"),
                            img = data.getString("urlToImage"),
                            author = data.getString("author"),
                            date = data.getString("publishedAt"),
                            description = data.getString("description"),
                            url = data.getString("url"),
                            source = data.getJSONObject("source").getString("name")
                        )
                        newsList.add(news)
                    }
                    adapter.setNewsData(newsList)
                } catch (e: Exception) {
                    Toast.makeText(this@NewsActivity, e.message, Toast.LENGTH_SHORT).show()
                } finally {
                    showLoading(false)
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                showLoading(false)
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@NewsActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun setUpActiveMenu(menu: Menu, pageIdx: Int) {
        super.setUpActiveMenu(menu, pageIdx)
    }

    override fun setUpMenuItemListener(
        bottomNav: BottomNavigationView,
        context: Context,
        currentPageIdx: Int
    ) {
        super.setUpMenuItemListener(bottomNav, context, currentPageIdx)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}