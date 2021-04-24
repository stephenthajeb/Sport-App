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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.sportapp.R
import com.example.sportapp.UI.TrainingTrackerActivity

class RunningTrackerService : Service(), SensorEventListener {
    private var sensorManager : SensorManager? = null
    private var isForeground : Boolean = false
    private var isTraining : Boolean = false

    companion object{
        const val EXTRA_IS_FOREGROUND="isForeground"
        const val EXTRA_IS_TRAINING="isTraining"
        const val ACTION_TRACKING = "ACTION_TRACKING"
        const val ACTION_STOP = "ACTION STOP"

        const val STEPS_TRACKED = "stepsTrack"
        const val NOTIF_TRACKING = 1
        const val TRAINING = "Training"
        const val NOTIF_CHANNEL_NAME="Running Training"
        const val NOTIF_TRACKING_REQUEST_CODE = 200
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("service","masuk")
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            isForeground = it.getBooleanExtra(EXTRA_IS_FOREGROUND, false)
            isTraining = it.getBooleanExtra(EXTRA_IS_TRAINING,false)
        }
        Log.d("service",isForeground.toString())
        setSensors(on=true)
        if (!isTraining){
            stopForeground(true)
            stopSelf()
            setSensors(on=false)
        }
        return START_STICKY
    }

    private fun setSensors(on:Boolean){
        sensorManager = getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
        val stepsSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        val dummySensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if (on){
            if (stepsSensor == null){
                Toast.makeText(this, "Step Sensor not found. Activate dummy sensor", Toast.LENGTH_SHORT).show()
                //Dummy sensor for testing Service. Todo: Delete later
                Toast.makeText(this, "Accelerometer", Toast.LENGTH_SHORT).show()
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
            sensorManager?.unregisterListener(this,stepsSensor)
            sensorManager?.unregisterListener(this,dummySensor)

        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER){
            Log.d("service","step count")
            val steps = event.values[0] //float
            val intent = Intent()
            intent.putExtra(STEPS_TRACKED, steps)
            intent.action = ACTION_TRACKING
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            if (isForeground){
                val notification = createNotification(steps)
                startForeground(NOTIF_TRACKING, notification)
            } else {
                stopForeground(true)
            }
        } else {
            //Todo: delete this block later
            val steps = event.values[0]
            val intent = Intent()
            intent.putExtra(STEPS_TRACKED, steps)
            intent.action = ACTION_TRACKING
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            if (isForeground){
                val notification = createNotification(steps)
                startForeground(NOTIF_TRACKING, notification)
            } else {
                stopForeground(true)
            }
        }
    }

    private fun createNotification(steps: Float): Notification {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val contentIntent = PendingIntent.getActivity(
            this, NOTIF_TRACKING_REQUEST_CODE, Intent(
                this,
                TrainingTrackerActivity::class.java
            ), PendingIntent.FLAG_UPDATE_CURRENT
        )

        //Todo: configure notification builder later
        val notificationBuilder = NotificationCompat.Builder(baseContext, TRAINING)
            .setContentTitle(resources.getString(R.string.app_name))
            .setContentText("You have run $steps in this training")
            .setWhen(System.currentTimeMillis())
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(null)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
//            .addAction()//cancel
//            .addAction()//training

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                TRAINING,
                NOTIF_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.setSound(null,null)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            notificationBuilder.setChannelId(TRAINING)
            notificationManager.createNotificationChannel(channel)
            //SetAutoCancel gimana wkwk ?
        }

        return notificationBuilder.build()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("service","destroy")
    }

}