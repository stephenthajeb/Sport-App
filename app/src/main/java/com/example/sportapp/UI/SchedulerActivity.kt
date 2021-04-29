package com.example.sportapp.UI

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.observe
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportapp.Data.Schedule
import com.example.sportapp.R
import com.example.sportapp.Receiver.TrainingReceiver
import com.example.sportapp.ScheduleModelFactory
import com.example.sportapp.ScheduleViewModel
import com.example.sportapp.Service.CyclingTrackerService
import com.example.sportapp.Service.RunningTrackerService
import com.example.sportapp.SportApp
import com.example.sportapp.UI.Adapter.ScheduleAdapter
import com.example.sportapp.databinding.ActivitySchedulerBinding
import java.util.*
import kotlin.collections.ArrayList

class SchedulerActivity : AppCompatActivity() {
    private lateinit var adapter: ScheduleAdapter
    private lateinit var binding: ActivitySchedulerBinding
    private val scheduleViewModel: ScheduleViewModel by viewModels {
        ScheduleModelFactory((application as SportApp).scheduleDAO)
    }
    private lateinit var trainingReceiver: TrainingReceiver

    companion object {
        const val REQ_ADD_SCHEDULE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchedulerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setActiveNavMenu()
        menuItemListener()
        setUpBtnListener()
        setUpAdapter()
        trainingReceiver = TrainingReceiver()
    }

    private fun setActiveNavMenu() {
        val menuItem: MenuItem = binding.bottomNavView.menu.getItem(3)
        menuItem.isChecked = true
    }

    private fun menuItemListener() {
        binding.bottomNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_news -> {
                    val intent = Intent(this, NewsActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_tracker -> {
                    val intent = Intent(this, TrainingTrackerActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_history -> {
                    val intent = Intent(this, HistoryActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_scheduler -> {
                    return@setOnNavigationItemSelectedListener true
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun setUpBtnListener() {
        binding?.fabAddNewSchedule?.setOnClickListener {
            val intent = Intent(this, SchedulerAddActivity::class.java)
            startActivityForResult(intent, REQ_ADD_SCHEDULE)
        }
    }


    private fun setUpAdapter() {
        adapter = ScheduleAdapter()
        binding.rvSchedule.adapter = adapter
        binding.rvSchedule.layoutManager = LinearLayoutManager(this)

        scheduleViewModel.schedules.observe(this) {
            adapter.setData(it as ArrayList<Schedule>)
        }

        if (scheduleViewModel.schedules.value?.size == 0) {
            //Todo : show icon indication empty
            Toast.makeText(this, "No schedules found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_ADD_SCHEDULE && resultCode == RESULT_OK) {
            val schedule = data?.getParcelableExtra<Schedule>(SchedulerAddActivity.EXTRA_SCHEDULE)
            try {
                schedule?.let {
                    scheduleViewModel.insert(schedule = it)
                    signalingReceiver(it)
                }

            } catch (e: Error) {
                Toast.makeText(this, "Error. Save failed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun signalingReceiver(schedule: Schedule) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        //Todo: change isTesting to false in production
        if (schedule.isAuto == 1) {
            trainingReceiver.setTrackingNotification(this,schedule,false)
        } else {
            trainingReceiver.setReminderNotification(this,schedule,true)
        }
    }

}