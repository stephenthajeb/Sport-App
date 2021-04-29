package com.example.sportapp.UI

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sportapp.Data.Schedule
import com.example.sportapp.IUseBottomNav
import com.example.sportapp.R
import com.example.sportapp.UI.Reusable.DatePickerFragment
import com.example.sportapp.UI.Reusable.TimePickerFragment
import com.example.sportapp.databinding.ActivitySchedulerAddBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*

class SchedulerAddActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener,
    TimePickerFragment.DialogTimeListener, IUseBottomNav {
    private lateinit var binding: ActivitySchedulerAddBinding
    private var date: String? = null
    private var startTime: String? = null
    private var endTime: String? = null

    companion object {
        const val RUNNING = "RUNNING"
        const val CYCLING = "CYCLING"
        const val FREQ_ONCE = "ONCE"
        const val FREQ_EVERYDAY = "EVERYDAY"
        const val FREQ_CUSTOM = "CUSTOM"
        private const val DATE_PICKER_TAG = "DatePicker"
        private const val TIME_PICKER_START_TAG = "TimePickerStart"
        private const val TIME_PICKER_END_TAG = "TimePickerEnd"
        const val EXTRA_SCHEDULE = "Schedule"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchedulerAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNotSetDate()
        setUpBtnListener()
        setUpActiveMenu(binding.bottomNavView.menu,3)
        setUpMenuItemListener(binding.bottomNavView,this,3)
    }

    //Todo: onSaveInstanceState later

    private fun setupNotSetDate() {
        binding.tStart.text = "Time Start: Not Set"
        binding.tEnd.text = "Time End: Not Set"
    }

    private fun setUpBtnListener() {
        val timePicker = TimePickerFragment()

        binding.rgFreq.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_freq_custom -> {
                    binding.llDateContainer.visibility = View.GONE
                    binding.llDaysContainer.visibility = View.VISIBLE
                    date = null
                }
                R.id.rb_freq_everyday -> {
                    binding.llDateContainer.visibility = View.GONE
                    binding.llDaysContainer.visibility = View.GONE
                }
                R.id.rb_freq_once -> {
                    binding.llDateContainer.visibility = View.VISIBLE
                    binding.llDaysContainer.visibility = View.GONE
                }
            }
        }

        binding.btnDatePicker.setOnClickListener {
            val datePicker = DatePickerFragment()
            datePicker.show(supportFragmentManager, DATE_PICKER_TAG)
        }

        binding.btnTimeStartPicker.setOnClickListener {
            timePicker.show(supportFragmentManager, TIME_PICKER_START_TAG)
        }

        binding.btnTimeEndPicker.setOnClickListener {
            timePicker.show(supportFragmentManager, TIME_PICKER_END_TAG)
        }

        onSubmit()
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        date = formatter.format(calendar.time)
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        when (tag) {
            TIME_PICKER_START_TAG -> {
                startTime = formatter.format(calendar.time)
                val st = startTime.toString()
                binding.tStart.text = "Time Start: $st"

            }
            else -> {
                endTime = formatter.format(calendar.time)
                val et = endTime.toString()
                binding.tEnd.text = "Time End: $et"
            }
        }
    }

    private fun getCustomDaysSelected(): List<Int> {
        val days = mutableListOf<Int>()
        if (binding.cbSun.isChecked) days.add(1)
        if (binding.cbMon.isChecked) days.add(2)
        if (binding.cbTues.isChecked) days.add(3)
        if (binding.cbWed.isChecked) days.add(4)
        if (binding.cbThur.isChecked) days.add(5)
        if (binding.cbFri.isChecked) days.add(6)
        if (binding.cbSat.isChecked) days.add(7)
        return days
    }

    private fun onSubmit(){
        binding.btnSave.setOnClickListener {
            val tempMode = binding.rgMode.checkedRadioButtonId //return -1 if none checked
            val tempFreq = binding.rgFreq.checkedRadioButtonId
            val tempDays = getCustomDaysSelected()
            val tempTarget = binding.etTarget.text
            val tempIsAuto = if (binding.cbIsAuto.isChecked) 1 else 0

            var msg = "Schedule Save"

            if (tempMode == -1) {
                msg = "Training mode not set!!"
            } else if (tempFreq == -1) {
                msg = "Schedule frequency not set!!"
            } else if ( tempTarget == null || tempTarget.toString() == "") {
                msg = "Training target not set!!"
            } else if (startTime == null || endTime == null) {
                msg = "Training time not set!!"
            } else if (tempFreq == R.id.rb_freq_custom && tempDays.isEmpty()) {
                msg = "Custom training days not set!!"
            } else if (tempFreq == R.id.rb_freq_once && date == null) {
                msg = "Training date not set !!"
            } else {
                val mode = if (tempMode == R.id.rb_running) RUNNING else CYCLING
                var freqMode: String? = null
                freqMode = when (tempFreq) {
                    R.id.rb_freq_custom -> FREQ_CUSTOM
                    R.id.rb_freq_everyday -> FREQ_EVERYDAY
                    else -> FREQ_ONCE
                }

                val schedule = Schedule(
                    mode = mode,
                    frequency = freqMode,
                    target = tempTarget.toString().toDouble(),
                    isAuto = tempIsAuto,
                    startTime = startTime,
                    finishTime = endTime,
                    days = if (tempFreq == R.id.rb_freq_custom) tempDays.joinToString(separator = ",") else null
                )
                if (date != null) schedule.date = date

                val data = Intent()
                data.putExtra(EXTRA_SCHEDULE,schedule)
                setResult(RESULT_OK,data)
                finish()
            }
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun setUpActiveMenu(menu: Menu, pageIdx: Int) {
        super.setUpActiveMenu(menu, pageIdx)
    }

    override fun setUpMenuItemListener(
        bottomNav: BottomNavigationView,
        context: Context,
        currentPageIdx: Int
    ) {
        super.setUpMenuItemListener(bottomNav, context, currentPageIdx)
    }


}