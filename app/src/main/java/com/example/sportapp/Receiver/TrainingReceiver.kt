package com.example.sportapp.Receiver

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.sportapp.Data.Schedule
import com.example.sportapp.R
import com.example.sportapp.Service.CyclingTrackerService
import com.example.sportapp.Service.RunningTrackerService
import com.example.sportapp.UI.SchedulerAddActivity
import java.util.*
import kotlin.collections.ArrayList

class TrainingReceiver : BroadcastReceiver() {
    companion object {
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_NOTIF_TYPE = "type"
        const val NOTIF_REMIND = "remind"
        const val NOTIF_TRACKING = "tracking"
        const val EXTRA_NOTIF_ID = "notif id"
        const val NOTIF_TITLE = "Sport App"
        const val EXTRA_ICON = "icon"
        const val EXTRA_SCHEDULE = "schedule"
        const val EXTRA_MODE = "mode"
        const val NOTIF_STOP_TRACKING = "stop tracking"
        const val REQ_CODE_REMIND = 0
        const val REQ_CODE_TRACKING = 1
        const val REQ_CODE_STOP = 2
    }

    override fun onReceive(context: Context, intent: Intent) {
        val notifType = intent.getStringExtra(EXTRA_NOTIF_TYPE)
        val notifMessage = intent.getStringExtra(EXTRA_MESSAGE)
        val notifIcon = intent.getIntExtra(EXTRA_ICON, -1)
        val notifId = intent.getIntExtra(EXTRA_NOTIF_ID, -1)
        val mode = intent.getStringExtra(EXTRA_MODE)

        Log.d("test", "notifType $notifType")
        if (notifType == NOTIF_REMIND && notifId != -1 && notifMessage != null && notifIcon != -1) {
            Log.d("test", "start notify")
            showReminderNotification(context, notifMessage, notifId)
        }
        if (notifType == NOTIF_TRACKING && notifId != -1) {
            Log.d("test", "start traacking")
            if (mode == SchedulerAddActivity.RUNNING) {
                val intent = Intent(context, RunningTrackerService::class.java)
                intent.putExtra(RunningTrackerService.EXTRA_IS_FOREGROUND, true)
                intent.putExtra(RunningTrackerService.EXTRA_IS_TRAINING, true)
                context.startService(intent)
            } else {

            }
        }

        if (notifType == NOTIF_STOP_TRACKING && notifId != -1) {
            val trackingService =
                if (mode == SchedulerAddActivity.RUNNING) RunningTrackerService::class.java else CyclingTrackerService::class.java
            val intent = Intent(context, trackingService)
            intent.putExtra(RunningTrackerService.EXTRA_IS_FOREGROUND, true)
            intent.putExtra(RunningTrackerService.EXTRA_IS_TRAINING, false)
            context.startService(intent)
        }
    }

    fun setTrackingNotification(context: Context, schedule: Schedule, isTestingMode: Boolean) {
        /* Schedule start training */
        val intent = Intent(context, TrainingReceiver::class.java)
        intent.putExtra(EXTRA_NOTIF_TYPE, NOTIF_TRACKING)
        intent.putExtra(EXTRA_NOTIF_ID, REQ_CODE_TRACKING)
        intent.putExtra(EXTRA_MODE, schedule.mode)
        val pendingIntent = PendingIntent.getBroadcast(context, REQ_CODE_TRACKING, intent, 0)
        setUpAlarmManager(context, pendingIntent, schedule, isTestingMode, true)

        /* Schedule finish training */
        val stopIntent = Intent(context, TrainingReceiver::class.java)
        stopIntent.putExtra(EXTRA_NOTIF_TYPE, NOTIF_STOP_TRACKING)
        stopIntent.putExtra(EXTRA_NOTIF_ID, REQ_CODE_STOP)
        stopIntent.putExtra(EXTRA_MODE, schedule.mode)
        val stopPendingIntent = PendingIntent.getBroadcast(context, REQ_CODE_STOP, stopIntent,0)
        setUpAlarmManager(context, stopPendingIntent, schedule, isTestingMode, false)
    }


    fun setReminderNotification(
        context: Context,
        schedule: Schedule,
        isTestingMode: Boolean
    ) {
        var message = ""
        var icon = -1

        if (schedule.mode == SchedulerAddActivity.RUNNING) {
            icon = R.drawable.ic_baseline_directions_run_50
            message =
                "You have a running training at ${schedule.startTime} - ${schedule.finishTime}"
        } else {
            icon = R.drawable.ic_baseline_directions_bike_50
            message =
                "You have a cycling training at ${schedule.startTime} - ${schedule.finishTime}"
        }
        val intent = Intent(context, TrainingReceiver::class.java)
        intent.putExtra(EXTRA_NOTIF_TYPE, NOTIF_REMIND)
        intent.putExtra(EXTRA_NOTIF_ID, REQ_CODE_REMIND)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_ICON, icon)
        val pendingIntent = PendingIntent.getBroadcast(context, REQ_CODE_REMIND, intent, 0)
        setUpAlarmManager(context, pendingIntent, schedule, isTestingMode, true)
    }


    private fun setUpAlarmManager(
        context: Context,
        pendingIntent: PendingIntent,
        schedule: Schedule,
        isTestingMode: Boolean,
        isStartSchedule: Boolean
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendarList: ArrayList<Calendar> = getSchedulesCalendar(schedule, isStartSchedule)

        if (isTestingMode) {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                Calendar.getInstance().timeInMillis,
                pendingIntent
            )
        } else {
            when (schedule.frequency) {
                SchedulerAddActivity.FREQ_ONCE -> {
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        calendarList[0].timeInMillis,
                        pendingIntent
                    )
                }
                SchedulerAddActivity.FREQ_EVERYDAY -> {
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendarList[0].timeInMillis,
                        24 * 60 * 60 * 1000,
                        pendingIntent
                    )
                }
                SchedulerAddActivity.FREQ_CUSTOM -> {
                    calendarList.forEach { it ->
                        alarmManager.setRepeating(
                            AlarmManager.RTC_WAKEUP,
                            it.timeInMillis,
                            7 * 24 * 60 * 60 * 1000,
                            pendingIntent
                        )
                    }
                }
            }
        }
    }

    //Todo: fix notification only shows the oldest notif.
    private fun showReminderNotification(context: Context, message: String, notifId: Int) {
        val channelId = "Channel_1"
        val channelName = "Training Reminder"

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(NOTIF_TITLE)
            .setContentText(message)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setSmallIcon(R.drawable.ic_baseline_directions_run_50)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
    }

    private fun getSchedulesCalendar(
        schedule: Schedule,
        isStartSchedule: Boolean
    ): ArrayList<Calendar> {
        val timeArr = if (isStartSchedule) schedule.startTime!!.split(":").map { it.toInt() }.toTypedArray()
        else schedule.finishTime!!.split(":").map { it.toInt() }.toTypedArray()

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
                    calendar.set(Calendar.HOUR_OF_DAY, timeArr[0])
                    calendar.set(Calendar.MINUTE, timeArr[1])
                    calendarList.add(calendar)
                }
            }
        }
        return calendarList
    }

    private fun cancelSchedule() {}


}