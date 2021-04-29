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
        const val REQ_START_ALARM = 0
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
                    Log.d("test", "schedule add")
                    addAlarmManager(it)
                }

            } catch (e: Error) {
                Toast.makeText(this, "Error. Save failed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun addAlarmManager(schedule: Schedule) {
        Log.d("test", "add alarm")
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val trackingService =
            if (schedule.mode == SchedulerAddActivity.RUNNING) RunningTrackerService::class.java else CyclingTrackerService::class.java
        val intent = Intent(this, trackingService)

        if (schedule.isAuto == 1) {
            intent.putExtra(RunningTrackerService.EXTRA_IS_FOREGROUND, true)
            intent.putExtra(RunningTrackerService.EXTRA_IS_TRAINING, true)
            val pendingIntent =
                PendingIntent.getService(this, schedule.id, intent, 0)
            autoStartTracker(schedule, pendingIntent, alarmManager)
        } else {
            scheduleReminderNotification(schedule)
        }

    }

    private fun getSchedulesCalendar(schedule:Schedule):ArrayList<Calendar>{
        val timeArr = schedule.startTime!!.split(":").map { it.toInt() }.toTypedArray()
        val calendarList = ArrayList<Calendar>()
        when (schedule.frequency) {
            SchedulerAddActivity.FREQ_ONCE -> {
                val dateArr = schedule.date!!.split("-").map { it.toInt() }.toTypedArray()
                val calendar = Calendar.getInstance()
                calendar.set(
                    dateArr[0],
                    dateArr[1],
                    dateArr[2],
                    timeArr[0],
                    timeArr[1]
                )//year,month,date,hour,minute
                calendarList.add(calendar)
            }
            SchedulerAddActivity.FREQ_EVERYDAY -> {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, timeArr[0])
                calendar.set(Calendar.MINUTE, timeArr[1])
                calendarList.add(calendar)
            }
            SchedulerAddActivity.FREQ_CUSTOM -> {
                val days = schedule.days!!.split(",").map { it.toInt() }.toTypedArray()
                days.forEach { it ->
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.DAY_OF_WEEK, it)
                    calendarList.add(calendar)
                }
            }
        }
        return calendarList
    }

    private fun autoStartTracker(
        schedule: Schedule,
        pendingIntent: PendingIntent,
        alarmManager: AlarmManager
    ) {
        Log.d("test", "service auto start")
        val timeArr = schedule.startTime!!.split(":").map { it.toInt() }.toTypedArray()
        when (schedule.frequency) {
            SchedulerAddActivity.FREQ_ONCE -> {
                val dateArr = schedule.date!!.split("-").map { it.toInt() }.toTypedArray()
                val calendar = Calendar.getInstance()
                calendar.set(
                    dateArr[0],
                    dateArr[1],
                    dateArr[2],
                    timeArr[0],
                    timeArr[1]
                )//year,month,date,hour,minute
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
            SchedulerAddActivity.FREQ_EVERYDAY -> {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, timeArr[0])
                calendar.set(Calendar.MINUTE, timeArr[1])
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    24 * 60 * 60 * 1000,
                    pendingIntent
                )
            }
            SchedulerAddActivity.FREQ_CUSTOM -> {
                val days = schedule.days!!.split(",").map { it.toInt() }.toTypedArray()
                days.forEach { it ->
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.DAY_OF_WEEK, it)
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        7 * 24 * 60 * 60 * 1000,
                        pendingIntent
                    )
                }
            }
        }
    }

    private fun scheduleReminderNotification(schedule: Schedule) {
        val channelId = "Training"
        val channelName: String
        var icon : Int
        var message:String=""
        Log.d("test","showReminder")

        val notificationManagerCompat = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        if (schedule.mode == SchedulerAddActivity.RUNNING) {
            icon = R.drawable.ic_baseline_directions_run_50
            channelName = "Running Training"
            message = "You have a running training at ${schedule.startTime} - ${schedule.finishTime}"
        } else {
            icon = R.drawable.ic_baseline_directions_bike_50
            channelName = "Cycling Training"
            message = "You have a cycling training at ${schedule.startTime} - ${schedule.finishTime}"
        }
        val builder = NotificationCompat.Builder(baseContext, RunningTrackerService.TRAINING)
            .setSmallIcon(icon)
            .setContentTitle(resources.getString(R.string.app_name))
            .setContentText(message)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create or update. */
            val channel = NotificationChannel(channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT)

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            builder.setChannelId(channelId)

            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(1, notification)
    }





}