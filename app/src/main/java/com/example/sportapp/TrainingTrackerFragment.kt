package com.example.sportapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sportapp.databinding.FragmentTrainingTrackerBinding

private const val TRAINING_MODE = "training_mode"


class TrainingTrackerFragment : Fragment() {
    private var trainingMode: String? = null
    private var binding: FragmentTrainingTrackerBinding? = null

    companion object {
        const val RUNNING_MODE = "running"
        const val CYCLING_MODE = "cycling"

        @JvmStatic
        fun newInstance(position:Int) =
            TrainingTrackerFragment().apply {
                trainingMode = if (position === 0) RUNNING_MODE else CYCLING_MODE
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrainingTrackerBinding.inflate(inflater, container, false)
        showTrainingMode()
        return binding!!.root
    }

    private fun showTrainingMode() {
        if (binding!==null){
            Log.d("test","b")
            if (trainingMode == RUNNING_MODE) {
                Log.d("test","c")
                binding!!.tvTrainingModeIndicator.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    R.drawable.ic_baseline_directions_run_50,
                    0,
                    0
                )
                binding!!.tvTrainingModeIndicator.text ="Running Mode"
            } else {
                Log.d("test","d")
                binding!!.tvTrainingModeIndicator.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    R.drawable.ic_baseline_directions_bike_50,
                    0,
                    0
                )
                binding!!.tvTrainingModeIndicator.text = "Cycling Mode"
            }
        }
    }
}