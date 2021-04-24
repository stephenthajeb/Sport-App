package com.example.sportapp.UI

import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.sportapp.IUseBottomNav
import com.example.sportapp.databinding.ActivityNewsDetailBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewsDetailActivity : AppCompatActivity(), IUseBottomNav {
    private lateinit var binding: ActivityNewsDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "News Detail"
        setContentView(binding.root)
        setUpWebView()
        setUpActiveMenu(binding.bottomNavView.menu,0)
        setUpMenuItemListener(binding.bottomNavView,this,0)
    }

    private fun setUpWebView(){
        val url = intent.getStringExtra(NewsActivity.EXTRA_URL)
        binding.wvNews.settings.javaScriptEnabled = true
        binding.wvNews.setInitialScale(1)
        binding.wvNews.settings.loadWithOverviewMode=true
        binding.wvNews.settings.useWideViewPort=true
        binding.wvNews.settings.builtInZoomControls=true
        binding.wvNews.settings.displayZoomControls=false

        if (url !== null) {
            binding.wvNews.webViewClient = object : WebViewClient() {

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    showLoading(true)
                }
                override fun onPageFinished(view: WebView, url: String) {
                    showLoading(false)
                }
            }
            try{
                binding.wvNews.loadUrl(url)

            } catch(e:Exception){
                Toast.makeText(this@NewsDetailActivity,e.message,Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
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
}