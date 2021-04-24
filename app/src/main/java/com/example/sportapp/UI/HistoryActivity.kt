package com.example.sportapp.UI

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.example.sportapp.IUseBottomNav
import com.example.sportapp.R
import com.example.sportapp.databinding.ActivityHistoryBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HistoryActivity : AppCompatActivity(), IUseBottomNav {
    private lateinit var binding: ActivityHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActiveMenu(binding.bottomNavView.menu,2)
        setUpMenuItemListener(binding.bottomNavView,this,2)
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