package com.example.sportapp.UI

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.sportapp.*
import com.example.sportapp.Data.History
import com.example.sportapp.databinding.ActivityHistoryDetailTrainingBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

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
        setUpActiveMenu(binding.bottomNavView.menu,2)
        setUpMenuItemListener(binding.bottomNavView,this,2)
        history = intent.getParcelableExtra(HistoryDetailFragment.EXTRA_HISTORY)
        binding.dateDetail.text = history?.date
        binding.endDate.text = history?.endTime
        binding.startDate.text  = history?.startTime
        binding.imageHistory.setImageBitmap(history?.img)
        binding.mode.text = history?.mode
        binding.result.text = history?.result.toString()
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