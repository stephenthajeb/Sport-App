package com.example.sportapp.UI

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportapp.Data.Schedule
import com.example.sportapp.R
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

    companion object {
        const val REQ_ADD_SCHEDULE = 1
        const val REQ_START_ALARM = 2
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
                    addAlarmManagerForTracking(it)
                }

            } catch (e: Error) {
                Toast.makeText(this, "Error. Save failed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun addAlarmManagerForTracking(schedule: Schedule) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val trackingService =
            if (schedule.mode == SchedulerAddActivity.RUNNING) RunningTrackerService::class.java else CyclingTrackerService::class.java
        val intent = Intent(this, trackingService)
        val pendingIntent = PendingIntent.getService(this, REQ_START_ALARM,intent,PendingIntent.FLAG_IMMUTABLE)
        if (schedule.isAuto == 1) autoStartTracker(schedule,pendingIntent,alarmManager)
        //Todo: SetNotification
    }

    fun autoStartTracker(schedule: Schedule,pendingIntent:PendingIntent,alarmManager:AlarmManager){
        val timeArr = schedule.startTime!!.split(":").map{ it.toInt() }.toTypedArray()
        when (schedule.frequency){
            SchedulerAddActivity.FREQ_ONCE->{
                val dateArr = schedule.date!!.split("-").map{ it.toInt() }.toTypedArray()
                val calendar = Calendar.getInstance()
                calendar.set(dateArr[0],dateArr[1],dateArr[2],timeArr[0],timeArr[1])//year,month,date,hour,minute
                alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pendingIntent)
            }
            SchedulerAddActivity.FREQ_EVERYDAY->{
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, timeArr[0])
                calendar.set(Calendar.MINUTE, timeArr[1])
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,24*60*60*1000, pendingIntent)
            }
            SchedulerAddActivity.FREQ_CUSTOM->{
                val days = schedule.days!!.split(",").map{it.toInt()}.toTypedArray()
                days.forEach { it->
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.DAY_OF_WEEK,it)
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,7*24*60*60*1000,pendingIntent)
                }
            }
        }
    }

}