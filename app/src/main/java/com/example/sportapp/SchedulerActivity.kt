package com.example.sportapp

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportapp.databinding.ActivityNewsBinding
import com.example.sportapp.databinding.ActivitySchedulerBinding

class SchedulerActivity : AppCompatActivity(){
    private lateinit var adapter: ScheduleAdapter
    private lateinit var binding: ActivitySchedulerBinding
    private val scheduleViewModel:ScheduleViewModel by viewModels{
        ScheduleModelFactory((application as SportApp).scheduleDAO)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchedulerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setActiveNavMenu()
        menuItemListener()
        setUpBtnListener()
        setUpAdapter()
    }

    private fun setActiveNavMenu() {
        val menuItem: MenuItem = binding.bottomNavView.menu.getItem(3)
        menuItem.isChecked = true
    }

    private fun menuItemListener() {
        binding.bottomNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_news -> {
                    val intent = Intent(this,NewsActivity::class.java)
                    startActivity(intent)
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
                    return@setOnNavigationItemSelectedListener true
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun setUpBtnListener(){
        binding?.fabAddNewSchedule?.setOnClickListener{
            val intent = Intent(this,SchedulerAddActivity::class.java)
            startActivity(intent)
        }
    }


    private fun setUpAdapter(){
        adapter = ScheduleAdapter()
        binding.rvSchedule.adapter = adapter
        binding.rvSchedule.layoutManager = LinearLayoutManager(this)

        scheduleViewModel.schedules.observe(this){
            adapter.setData(it as ArrayList<Schedule>)
        }

    }

    //private fun getSchedulesFromDB(){
    //}

}