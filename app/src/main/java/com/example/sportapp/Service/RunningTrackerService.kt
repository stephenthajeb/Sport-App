package com.example.sportapp.Service

import android.app.*
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.sportapp.Data.History
import com.example.sportapp.Data.HistoryDAO
import com.example.sportapp.HistoryModelFactory
import com.example.sportapp.HistoryViewModel
import com.example.sportapp.R
import com.example.sportapp.SportApp
import com.example.sportapp.UI.SchedulerAddActivity
import com.example.sportapp.UI.TrainingTrackerActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.util.*

class RunningTrackerService : Service(), SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var isForeground: Boolean = false
    private var isTraining: Boolean = false
    private var steps: Float = 0f
    private var startTime: String? = null
    private var startDate: String? = null

    companion object {
        const val EXTRA_IS_FOREGROUND = "isForeground"
        const val EXTRA_IS_TRAINING = "isTraining"
        const val ACTION_TRACKING = "ACTION_TRACKING"
        const val ACTION_STOP = "ACTION STOP"
        const val ACTION_SAVING = "ACTION SAVING"
        const val STEPS_TRACKED = "stepsTrack"
        const val NOTIF_TRACKING = 1
        const val TRAINING = "Training"
        const val NOTIF_CHANNEL_NAME = "Running Training"
        const val NOTIF_TRACKING_REQUEST_CODE = 200
        const val EXTRA_HISTORY = "history"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("test", "service start")
        Toast.makeText(this, "Tracker service starts", Toast.LENGTH_SHORT).show()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            isForeground = it.getBooleanExtra(EXTRA_IS_FOREGROUND, false)
            isTraining = it.getBooleanExtra(EXTRA_IS_TRAINING, false)
        }
        setSensors(on = true)
        if (!isTraining) {
            Toast.makeText(this, "Tracker services stop", Toast.LENGTH_SHORT).show()
            stopForeground(true)
            stopSelf()
            setSensors(on = false)
        }
        val now = Calendar.getInstance()
        startDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(now.time)
        startTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(now.time)
        return START_STICKY
    }

    private fun setSensors(on: Boolean) {
        sensorManager = getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
        val stepsSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        val dummySensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if (on) {
            if (stepsSensor == null) {
                Toast.makeText(
                    this,
                    "Step Sensor not found. Activate Accelerometer",
                    Toast.LENGTH_LONG
                ).show()
                sensorManager?.registerListener(
                    this,
                    dummySensor,
                    SensorManager.SENSOR_DELAY_NORMAL,
                    SensorManager.SENSOR_DELAY_UI
                )
            } else {
                sensorManager?.registerListener(
                    this,
                    stepsSensor,
                    SensorManager.SENSOR_DELAY_NORMAL,
                    SensorManager.SENSOR_DELAY_UI
                )
            }
        } else {
            sensorManager?.unregisterListener(this, stepsSensor)
            sensorManager?.unregisterListener(this, dummySensor)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            steps = event.values[0] //float
            val intent = Intent()
            intent.putExtra(STEPS_TRACKED, steps)
            intent.action = ACTION_TRACKING
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            if (isForeground) {
                val notification = createNotification(steps)
                startForeground(NOTIF_TRACKING, notification)
            } else {
                stopForeground(true)
            }
        }
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) { //Dummy sensor
            steps = kotlin.math.abs(event.values[0])
            val intent = Intent()
            intent.putExtra(STEPS_TRACKED, steps)
            intent.action = ACTION_TRACKING
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            if (isForeground) {
                val notification = createNotification(steps)
                startForeground(NOTIF_TRACKING, notification)
            } else {
                stopForeground(true)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    private fun createNotification(steps: Float): Notification {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val contentIntent = PendingIntent.getActivity(
            this, NOTIF_TRACKING_REQUEST_CODE, Intent(
                this,
                TrainingTrackerActivity::class.java
            ), PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(baseContext, TRAINING)
            .setContentTitle(resources.getString(R.string.app_name))
            .setContentText("You have run $steps in this training")
            .setWhen(System.currentTimeMillis())
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(null)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentIntent(contentIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                TRAINING,
                NOTIF_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.setSound(null, null)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            notificationBuilder.setChannelId(TRAINING)
            notificationManager.createNotificationChannel(channel)
        }

        return notificationBuilder.build()
    }


    override fun onDestroy() {
        super.onDestroy()
        val appScope = CoroutineScope(SupervisorJob())
        appScope.launch(Dispatchers.IO) {
            saveToHistoryDb()
        }

    }

    private suspend fun saveToHistoryDb() {
        val history = History(
            mode = SchedulerAddActivity.RUNNING,
            result = steps,
            date = startDate,
            startTime = startTime,
            endTime = SimpleDateFormat(
                "HH:mm",
                Locale.getDefault()
            ).format(Calendar.getInstance().time)
        )
        (application as SportApp).historyDAO.insert(history)
    }

}