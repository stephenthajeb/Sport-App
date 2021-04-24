package com.example.sportapp

import android.content.Context
import android.content.Intent
import android.view.Menu
import com.example.sportapp.UI.HistoryActivity
import com.example.sportapp.UI.NewsActivity
import com.example.sportapp.UI.SchedulerActivity
import com.example.sportapp.UI.TrainingTrackerActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

interface IUseBottomNav {
    fun setUpActiveMenu(menu: Menu, pageIdx: Int){
        menu.getItem(pageIdx).isChecked = true
    }

    fun setUpMenuItemListener(bottomNav:BottomNavigationView,context:Context,currentPageIdx:Int){
        bottomNav.setOnNavigationItemSelectedListener { item ->
            var intent:Intent? = null
            when (item.itemId) {
                R.id.nav_news -> {
                    if (currentPageIdx==0) return@setOnNavigationItemSelectedListener true
                    intent = Intent(context, NewsActivity::class.java)
                }
                R.id.nav_tracker -> {
                    if (currentPageIdx==1) return@setOnNavigationItemSelectedListener true
                    intent = Intent(context, TrainingTrackerActivity::class.java)
                }
                R.id.nav_history->{
                    if (currentPageIdx==2) return@setOnNavigationItemSelectedListener true
                    intent = Intent(context, HistoryActivity::class.java)
                }
                R.id.nav_scheduler->{
                    if (currentPageIdx==3) return@setOnNavigationItemSelectedListener true
                    intent = Intent(context, SchedulerActivity::class.java)
                }
            }
            intent?.let{it -> context.startActivity(it)}
            return@setOnNavigationItemSelectedListener true
        }
    }
}