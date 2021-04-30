package com.example.sportapp.UI

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.sportapp.*
import com.example.sportapp.Data.History
import com.example.sportapp.databinding.ActivityHistoryDetailTrainingBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*

class HistoryDetailTrainingActivity : AppCompatActivity(),IUseBottomNav {

    companion object{
        var history: History?= null
    }

    private lateinit var binding: ActivityHistoryDetailTrainingBinding
    private val historyViewModel: HistoryViewModel by viewModels{
        HistoryModelFactory((this.application as SportApp).historyDAO)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryDetailTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActiveMenu(binding.bottomNavView.menu, 2)
        setUpMenuItemListener(binding.bottomNavView, this, 2)
        history = intent.getParcelableExtra(HistoryDetailFragment.EXTRA_HISTORY)
        if(history == null){
            history = intent.extras?.getParcelable(HistoryDetailFragment.EXTRA_HISTORY)
        }

        val formatter = SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault())
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(history?.date)
        val buildDate = formatter.format(date).toString()
        val endTime = history?.endTime
        val startTime = history?.startTime

        binding.dateDetail.text = "Your training on $buildDate"
        binding.endDate.text = "Your training end on $endTime"
        binding.startDate.text  = "Your training start on $startTime"
        var bytes : ByteArray? = intent.getByteArrayExtra("Bitmap")
        if(bytes==null){
            bytes = intent.extras?.getByteArray("Bitmap")
            if(bytes!=null){
                binding.imageHistory.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
            }
        }
        else{
            binding.imageHistory.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
        }
        val result = history?.result
        binding.mode.text = history?.mode
        binding.result.text = "Your distance $result m"
        if(history?.mode == SchedulerAddActivity.RUNNING){
            binding.logoMode.setImageResource(R.drawable.ic_baseline_directions_run_50)
        }
        else{
            binding.logoMode.setImageResource(R.drawable.ic_baseline_directions_bike_50)
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