package com.example.sportapp.UI

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
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
import com.example.sportapp.Service.RunningTrackerService
import com.example.sportapp.UI.Adapter.TrainingTrackerFragmentAdapter
import com.example.sportapp.databinding.FragmentRunningTrackerBinding

class RunningTrackerFragment : Fragment() {
    private var isTraining: Boolean = false
    private var steps: Float = 0f
    private var binding: FragmentRunningTrackerBinding? = null
    private val historyViewModel: HistoryViewModel by viewModels{
        HistoryModelFactory((activity?.application as SportApp).historyDAO)
    }


    companion object{
        const val EXTRA_STEPS="steps"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity !== null) LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
            trackerReceiver,
            IntentFilter(RunningTrackerService.ACTION_TRACKING)
        )
        if (savedInstanceState != null){
            steps = savedInstanceState.getFloat(EXTRA_STEPS,0f)
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
        setForegroundTracking(false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(RunningTrackerService.EXTRA_IS_TRAINING, isTraining)
        outState.putFloat(EXTRA_STEPS, steps)
    }

    override fun onPause() {
        super.onPause()
        //when leave app show tracking as notification
        setForegroundTracking(true)
    }


    private fun setForegroundTracking(activate:Boolean){
        val intent = Intent(activity, RunningTrackerService::class.java)
        intent.putExtra(RunningTrackerService.EXTRA_IS_TRAINING,isTraining)
        if (isTraining){
            intent.putExtra(RunningTrackerService.EXTRA_IS_FOREGROUND, activate)
            context?.startService(intent)
        }
    }

    override fun onDestroy() {
        if (activity !== null) LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(trackerReceiver)
        super.onDestroy()
    }


    private fun trainingBtnListener() {
        binding?.btnTraining?.setOnClickListener {
            isTraining = !isTraining
            val intent = Intent(context, RunningTrackerService::class.java)
            intent.putExtra(RunningTrackerService.EXTRA_IS_FOREGROUND,false)
            intent.putExtra(RunningTrackerService.EXTRA_IS_TRAINING,isTraining)
            context?.startService(intent)
            if (isTraining) {
                Toast.makeText(context,"Start training: walking/running",Toast.LENGTH_SHORT).show()
                binding?.btnTraining?.text = "Finish now"
            } else {
                Toast.makeText(context,"Saving training record",Toast.LENGTH_SHORT).show()
                binding?.btnTraining?.text = "Start Again"
                binding?.tvProgress?.text = ""
                try{
                    //Todo: add other field, navigate to historyDetailTraining
                    val history = History(mode=SchedulerAddActivity.RUNNING,result=steps)
                    historyViewModel.insert(history)
                    val intent = Intent(context,HistoryDetailTrainingActivity::class.java)
                    intent.putExtra(HistoryDetailFragment.EXTRA_HISTORY,history)
                    startActivity(intent)
                }catch(e:Error){
                    Toast.makeText(context,"Error in saving this record", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val trackerReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            steps =
                intent.getFloatExtra(RunningTrackerService.STEPS_TRACKED,steps)
            binding?.tvProgress?.text = "$steps steps"
        }
    }
}