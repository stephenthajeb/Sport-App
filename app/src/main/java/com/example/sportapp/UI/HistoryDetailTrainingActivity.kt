package com.example.sportapp.UI

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.sportapp.Data.History
import com.example.sportapp.IUseBottomNav
import com.example.sportapp.R
import com.example.sportapp.Service.RunningTrackerService
import com.example.sportapp.databinding.ActivityHistoryDetailTrainingBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HistoryDetailTrainingActivity : AppCompatActivity(),IUseBottomNav {
    private lateinit var binding: ActivityHistoryDetailTrainingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryDetailTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActiveMenu(binding.bottomNavView.menu,2)
        setUpMenuItemListener(binding.bottomNavView,this,2)
        val history = intent.getParcelableExtra<History>(RunningTrackerFragment.EXTRA_HISTORY)
        Log.d("history",history.toString())
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