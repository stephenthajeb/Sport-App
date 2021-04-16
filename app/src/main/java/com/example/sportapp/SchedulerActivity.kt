package com.example.sportapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.sportapp.databinding.ActivityNewsBinding
import com.example.sportapp.databinding.ActivitySchedulerBinding

class SchedulerActivity : AppCompatActivity() {
    private lateinit var adapter: ScheduleAdapter
    private lateinit var binding: ActivitySchedulerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchedulerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setActiveNavMenu()
        menuItemListener()
    }



    private fun setActiveNavMenu() {
        val menuItem: MenuItem = binding.bottomNavView.menu.getItem(0)
        menuItem.isChecked = true
    }

    private fun menuItemListener() {
        binding.bottomNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_news -> {
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_tracker -> {
                    val intent = Intent(this, TrainingTrackerActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_history->{
                    val intent = Intent(this,HistoryActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_scheduler->{
                    val intent = Intent(this,SchedulerActivity::class.java)
                    startActivity(intent)
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }
}