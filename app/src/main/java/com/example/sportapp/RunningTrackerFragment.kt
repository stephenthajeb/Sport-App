package com.example.sportapp

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
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.sportapp.databinding.FragmentRunningTrackerBinding

class RunningTrackerFragment : Fragment() {
    private var isTraining: Boolean = false
    private var steps: Float = 0f
    private var binding: FragmentRunningTrackerBinding? = null


    companion object{
        const val EXTRA_STEPS="steps"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity !== null) LocalBroadcastManager.getInstance(activity!!).registerReceiver(
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

//    private fun startForegroundServiceForTracking(foreground: Boolean) {
//        val intent = Intent(activity, RunningTrackerService::class.java)
//        intent.putExtra(RunningTrackerService.IS_FOREGROUND, foreground)
//        if (activity !== null) ContextCompat.startForegroundService(activity!!, intent)
//    }

    override fun onPause() {
        super.onPause()
        //when leave app show tracking as notification
        setForegroundTracking(true)
    }


    private fun setForegroundTracking(activate:Boolean){
        val intent = Intent(activity,RunningTrackerService::class.java)
        intent.putExtra(RunningTrackerService.EXTRA_IS_TRAINING,isTraining)
        if (isTraining){
            intent.putExtra(RunningTrackerService.EXTRA_IS_FOREGROUND, activate)
            context?.startService(intent)
        }
    }

    override fun onDestroy() {
        if (activity !== null) LocalBroadcastManager.getInstance(activity!!).unregisterReceiver(trackerReceiver)
        super.onDestroy()
    }


    private fun trainingBtnListener() {
        binding?.btnTraining?.setOnClickListener {
            isTraining = !isTraining
            val intent = Intent(context,RunningTrackerService::class.java)
            intent.putExtra(RunningTrackerService.EXTRA_IS_FOREGROUND,false)
            intent.putExtra(RunningTrackerService.EXTRA_IS_TRAINING,isTraining)
//            if (activity !== null) ContextCompat.startForegroundService(activity!!, intent)
            context?.startService(intent)
            if (isTraining) {
                Toast.makeText(context,"Start training: walking/running",Toast.LENGTH_SHORT).show()
                binding?.btnTraining?.text = "Finish now"
            } else {
                Toast.makeText(context,"Saving training record",Toast.LENGTH_SHORT).show()
                binding?.btnTraining?.text = "Start now"
                binding?.tvProgress?.text = ""
                //Todo : Save to DB, direct to history detail
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