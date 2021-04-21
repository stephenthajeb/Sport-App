package com.example.sportapp

//Todo: Delete this file later
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.sportapp.databinding.FragmentTrainingTrackerBinding

private const val TRAINING_MODE = "training_mode"


class TrainingTrackerFragment : Fragment() {
    private var trainingMode: String? = null
    private var isTraining: Boolean = false
    private var binding: FragmentTrainingTrackerBinding? = null


    companion object {
        const val RUNNING_MODE = "running"
        const val CYCLING_MODE = "cycling"


        @JvmStatic
        fun newInstance(position: Int) =
            TrainingTrackerFragment().apply {
                trainingMode = if (position == 0) RUNNING_MODE else CYCLING_MODE
                arguments = Bundle().apply {
                    putString(TRAINING_MODE, trainingMode)
                }
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            trainingMode = it.getString(TRAINING_MODE)
        }
        if (activity !== null) LocalBroadcastManager.getInstance(activity!!).registerReceiver(
            trackerReceiver,
            IntentFilter(TrainingTrackerServices.TRACK_RUNNING_ACTION)
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrainingTrackerBinding.inflate(inflater, container, false)
        showTrainingMode()
        trainingBtnListener()
        return binding!!.root
    }


    override fun onResume() {
        super.onResume()
        if (isTraining) {
            startForegroundServiceForTracking(false)
        }
    }

    private fun startForegroundServiceForTracking(background: Boolean) {
        val intent = Intent(activity, TrainingTrackerServices::class.java)
        intent.putExtra(TrainingTrackerServices.IS_FOREGROUND, background)
        if (activity !== null) ContextCompat.startForegroundService(activity!!, intent)
    }

    override fun onPause() {
        super.onPause()
        startForegroundServiceForTracking(true)
    }

    override fun onDestroy() {
        if (activity !== null) LocalBroadcastManager.getInstance(activity!!).unregisterReceiver(trackerReceiver)
        super.onDestroy()
    }


    private fun trainingBtnListener() {
        binding?.btnTraining?.setOnClickListener {
            isTraining = !isTraining
            if (!isTraining) {
                //Todo : Save to DB
            } else {
                startForegroundServiceForTracking(false)
            }
        }
    }

    private fun showTrainingMode() {
        Log.d("test", "b")
        if (trainingMode == RUNNING_MODE) {
            Log.d("test", "c")
            binding?.tvTrainingModeIndicator?.setCompoundDrawablesWithIntrinsicBounds(
                0,
                R.drawable.ic_baseline_directions_run_50,
                0,
                0
            )
            binding?.tvTrainingModeIndicator?.text = "Running Mode"
        } else {
            Log.d("test", "d")
            binding?.tvTrainingModeIndicator?.setCompoundDrawablesWithIntrinsicBounds(
                0,
                R.drawable.ic_baseline_directions_bike_50,
                0,
                0
            )
            binding?.tvTrainingModeIndicator?.text = "Cycling Mode"
        }
    }

    private val trackerReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("service","receive")
            val steps =
                intent.getFloatExtra(TrainingTrackerServices.STEPS_TRACKED,0f)
            binding?.tvProgress?.text = "$steps steps"
        }
    }
}