package com.example.sportapp.UI

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import com.example.sportapp.*
import com.example.sportapp.databinding.ActivityHistoryBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class HistoryActivity : AppCompatActivity(), IUseBottomNav{
    private lateinit var binding: ActivityHistoryBinding
    private val historyViewModel: HistoryViewModel by viewModels{
        HistoryModelFactory((application as SportApp).historyDAO)
    }

    companion object{
        const val EXTRA_DATE = "date"
        const val EXTRA_MONTH = "month"
        const val EXTRA_YEAR = "year"
        const val EXTRA_CALENDAR = "calendar"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActiveMenu(binding.bottomNavView.menu,2)
        setUpMenuItemListener(binding.bottomNavView,this,2)
        setUpDatePickerListener()
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

    private fun setUpDatePickerListener(){
        binding.cvCalendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val intent = Intent(this, HistoryDetailActivity::class.java)
            val preciseMonth = month+1//Month is 0 based idx so Desember is 11
            val cal = Calendar.getInstance()
            cal.set(year,month,dayOfMonth,0,0)
            intent.putExtra(EXTRA_CALENDAR,cal.time)
            startActivity(intent)
            Toast.makeText(this,"History on $dayOfMonth-$preciseMonth-$year",Toast.LENGTH_SHORT).show()
        }
    }

}