package com.example.sportapp.UI

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.sportapp.*
import com.example.sportapp.Data.History
import com.example.sportapp.Receiver.TrainingReceiver
import com.example.sportapp.Service.RunningTrackerService
import com.example.sportapp.UI.Adapter.TrainingTrackerFragmentAdapter
import com.example.sportapp.databinding.FragmentRunningTrackerBinding
import java.text.SimpleDateFormat
import java.util.*

class RunningTrackerFragment : Fragment() {
    companion object {
        const val EXTRA_STEPS = "steps"
    }

    private var isTraining: Boolean = false
    private var steps: Float = 0f
    private var binding: FragmentRunningTrackerBinding? = null
    private var startDate: Calendar? = null
    private val historyViewModel: HistoryViewModel by viewModels {
        HistoryModelFactory((activity?.application as SportApp).historyDAO)
    }
    private val trackerReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            steps =
                intent.getFloatExtra(RunningTrackerService.STEPS_TRACKED, steps)
            binding?.tvProgress?.text = "$steps steps"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalBroadcastManager.getInstance(requireActivity())
            .registerReceiver(
                trackerReceiver,
                IntentFilter(RunningTrackerService.ACTION_TRACKING)
            )


        if (savedInstanceState != null) {
            steps = savedInstanceState.getFloat(EXTRA_STEPS, 0f)
            isTraining = savedInstanceState.getBoolean(RunningTrackerService.EXTRA_IS_TRAINING)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRunningTrackerBinding.inflate(inflater, container, false)
        trainingBtnListener()
        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
        //when user in app, hide notification,track result shows in app UI
        if (isTraining) {
            setForegroundTracking(false)
            Toast.makeText(context, "Tracking as background service", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(RunningTrackerService.EXTRA_IS_TRAINING, isTraining)
        outState.putFloat(EXTRA_STEPS, steps)
    }

    override fun onPause() {
        super.onPause()
        //when leave app show tracking as notification
        if (isTraining) {
            setForegroundTracking(true)
            Toast.makeText(context, "Tracking as foreground service", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setForegroundTracking(activate: Boolean) {
        val intent = Intent(activity, RunningTrackerService::class.java)
        intent.putExtra(RunningTrackerService.EXTRA_IS_TRAINING, isTraining)
        if (isTraining) {
            intent.putExtra(RunningTrackerService.EXTRA_IS_FOREGROUND, activate)
            context?.startService(intent)
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(trackerReceiver)
        Toast.makeText(context, "Stop tracking", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }


    private fun trainingBtnListener() {
        binding?.btnTraining?.setOnClickListener {
            isTraining = !isTraining
            val intent = Intent(context, RunningTrackerService::class.java)
            intent.putExtra(RunningTrackerService.EXTRA_IS_FOREGROUND, false)
            intent.putExtra(RunningTrackerService.EXTRA_IS_TRAINING, isTraining)
            context?.startService(intent)
            if (isTraining) {
                Toast.makeText(context, "Start training: walking/running", Toast.LENGTH_SHORT)
                    .show()
                startDate = Calendar.getInstance()
                binding?.btnTraining?.text = "Finish now"
            } else {
                steps = 0f
                binding?.btnTraining?.text = "Start Now"
                binding?.tvProgress?.text = ""
                Toast.makeText(context, "Saving training record", Toast.LENGTH_SHORT).show()
                try {
                    val date =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(startDate?.time)
                    val startTime =
                        SimpleDateFormat("HH:mm", Locale.getDefault()).format(startDate?.time)
                    val endTime = SimpleDateFormat(
                        "HH:mm",
                        Locale.getDefault()
                    ).format(Calendar.getInstance().time)
                    val history = History(
                        mode = SchedulerAddActivity.RUNNING,
                        result = steps,
                        date = date,
                        startTime = startTime,
                        endTime = endTime
                    )
                    historyViewModel.insert(history)
                    val intent = Intent(context, HistoryDetailTrainingActivity::class.java)
                    intent.putExtra(HistoryDetailFragment.EXTRA_HISTORY, history)
                    startActivity(intent)
                } catch (e: Error) {
                    Toast.makeText(context, "Error in saving this record", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }


}