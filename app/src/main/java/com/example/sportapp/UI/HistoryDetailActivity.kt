package com.example.sportapp.UI

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.example.sportapp.IUseBottomNav
import com.example.sportapp.databinding.ActivityHistoryDetailBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HistoryDetailActivity : AppCompatActivity(), IUseBottomNav {
    private lateinit var binding: ActivityHistoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActiveMenu(binding.bottomNavView.menu,2)
        setUpMenuItemListener(binding.bottomNavView,this,2)
    }

}