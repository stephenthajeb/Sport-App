package com.example.sportapp.UI

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.sportapp.*
import com.example.sportapp.Data.History
import com.example.sportapp.databinding.ActivityHistoryDetailTrainingBinding
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class HistoryDetailTrainingActivity : AppCompatActivity(),IUseBottomNav {
    private val historyViewModel: HistoryViewModel by viewModels {
        HistoryModelFactory((application as SportApp).historyDAO)
    }
    companion object{
        var history: History?= null
    }

    private lateinit var binding: ActivityHistoryDetailTrainingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryDetailTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActiveMenu(binding.bottomNavView.menu, 2)
        setUpMenuItemListener(binding.bottomNavView, this, 2)
        Log.d("test","historydeteailTraining")

        try {
            history = intent.getParcelableExtra(HistoryDetailFragment.EXTRA_HISTORY)//Tracker
            if(history == null){//Detail History
                history = intent.extras?.getParcelable(HistoryDetailFragment.EXTRA_HISTORY)
                if (history == null) history = runBlocking { historyViewModel.getLastHistory() }
            }
            if (history != null){
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

                if(history?.mode == SchedulerAddActivity.RUNNING){
                    binding.logoMode.setImageResource(R.drawable.ic_baseline_directions_run_50)
                    binding.result.text = "Your distance $result m"
                }
                else{
                    binding.logoMode.setImageResource(R.drawable.ic_baseline_directions_bike_50)
                    binding.result.text = "Your distance $result steps"
                }
            } else {
                Toast.makeText(this,"Some Erorr happen. Try to refresh", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Error){
            Log.d("debug",e.message!!)
        }

    }

}