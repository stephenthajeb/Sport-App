package com.example.sportapp.UI

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sportapp.IUseBottomNav
import com.example.sportapp.UI.Adapter.TrainingTrackerFragmentAdapter
import com.example.sportapp.databinding.ActivityTrainingTrackerBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.math.round

class TrainingTrackerActivity : AppCompatActivity(), SensorEventListener, IUseBottomNav {
    private lateinit var binding: ActivityTrainingTrackerBinding
    private lateinit var sensorManager: SensorManager
    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)


    companion object {
        private var TAB_TITLES = arrayOf(
            "Running",
            "Cycling",
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainingTrackerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActiveMenu(binding.bottomNavView.menu,1)
        setUpMenuItemListener(binding.bottomNavView,this,1)
        setUpTabLayout()
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()
        activateCompassSensorListener()
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    private fun activateCompassSensorListener(){
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magneticField ->
            sensorManager.registerListener(
                this,
                magneticField,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    private fun getDirection(angle:Double):String{
        var direction =""
        if (angle >= 350 || angle <= 10)
            direction = "N"
        if (angle < 350 && angle > 280)
            direction = "NW"
        if (angle <= 280 && angle > 260)
            direction = "W"
        if (angle <= 260 && angle > 190)
            direction = "SW"
        if (angle <= 190 && angle > 170)
            direction = "S"
        if (angle <= 170 && angle > 100)
            direction = "SE"
        if (angle <= 100 && angle > 80)
            direction = "E"
        if (angle <= 80 && angle > 10)
            direction = "NE"

        return direction
    }

    override fun onSensorChanged(event: SensorEvent) {
        //ToDo: register listener on another thread using service
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
        }
        //Update sensor data to UI
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)
        val orientation = SensorManager.getOrientation(rotationMatrix, orientationAngles)
        val degrees = (Math.toDegrees(orientation[0].toDouble()) + 360.0) % 360.0
        val angle = round(degrees * 100) / 100
        val direction = getDirection(degrees)
        binding.tvDegree.text = "$angle $direction"
        binding.ivCompass.rotation = angle.toFloat() * -1
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun setUpTabLayout() {
        val tabLayoutAdapter = TrainingTrackerFragmentAdapter(this)
        binding.viewPager.adapter = tabLayoutAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = TAB_TITLES[position]
        }.attach()
        supportActionBar?.elevation = 0f
    }


}