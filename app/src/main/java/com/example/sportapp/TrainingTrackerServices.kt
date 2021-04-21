package com.example.sportapp

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager

//Todo: Delete this file later
class TrainingTrackerServices : Service(), SensorEventListener {
    private lateinit var sensorManager : SensorManager
    private var isForeground : Boolean = false

    companion object{
        const val IS_FOREGROUND="isForeground"
        const val STEPS_TRACKED = "stepsTrack"
        const val TRACK_RUNNING_ACTION = "TRACK_RUNNING"
        const val NOTIF_TRACKING = 1
        const val TRACKING_CHANNEL_ID = "Tracking Channel"
        const val NOTIF_CHANNEL_NAME="Tracking Training"
        const val NOTIF_TRACKING_REQUEST_CODE = 200
    }

    override fun onCreate() {
        super.onCreate()
        setUpSensors()
        val notification = createNotification(0f)
        startForeground(NOTIF_TRACKING, notification)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            isForeground = it.getBooleanExtra(IS_FOREGROUND, false)
        }

        return START_STICKY
    }

    private fun setUpSensors(){
        sensorManager = getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
        val stepsSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepsSensor == null){
            Toast.makeText(this, "Can't track your footstep. Sensor not found", Toast.LENGTH_SHORT).show()
        }
        sensorManager.registerListener(
            this,
            stepsSensor,
            SensorManager.SENSOR_DELAY_NORMAL,
            SensorManager.SENSOR_DELAY_UI
        )

    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER){
            val steps = event.values[0] //float
            val intent = Intent()
            intent.putExtra(STEPS_TRACKED, steps)
            intent.action = TRACK_RUNNING_ACTION
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            if (isForeground){
                val notification = createNotification(steps)
                startForeground(NOTIF_TRACKING, notification)
            } else {
                stopForeground(true)

            }
        }
    }

    private fun createNotification(steps: Float):Notification{
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val contentIntent = PendingIntent.getActivity(
            this, NOTIF_TRACKING_REQUEST_CODE, Intent(
                this,
                TrainingTrackerActivity::class.java
            ), PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationBuilder = NotificationCompat.Builder(baseContext, TRACKING_CHANNEL_ID)
            .setContentTitle(resources.getString(R.string.app_name))
            .setContentText("You have run $steps in this training")
            .setWhen(System.currentTimeMillis())
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(null)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentIntent(contentIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                TRACKING_CHANNEL_ID,
                NOTIF_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            notificationBuilder.setChannelId(TRACKING_CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        return notificationBuilder.build()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}