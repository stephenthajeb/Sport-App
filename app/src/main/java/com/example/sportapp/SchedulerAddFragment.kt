package com.example.sportapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.sportapp.databinding.FragmentSchedulerAddBinding

class SchedulerAddFragment : Fragment() {
    private var binding: FragmentSchedulerAddBinding? = null
    private var schedule: Schedule? = null

    companion object {
        const val FREQ_ONCE = "once"
        const val FREQ_EVERYDAY = "everyday"
        const val FREQ_CUSTOM = "custom"
        const val EXTRA_SCHEDULE = "Schedule"
        const val RUNNING = "running"
        const val CYCLING = "cycling"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("test","oncreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSchedulerAddBinding.inflate(inflater, container, false)
        setInitialInputValue(savedInstanceState)
        Log.d("test","on create view")
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("test","on view created")
        //binding?.btnNext?.setOnClickListener {
        //    val tempMode = binding?.rgMode?.checkedRadioButtonId //return -1 if none checked
        //    val tempFreq = binding?.rgFreq?.checkedRadioButtonId
        //    if (tempMode == -1 || tempFreq == -1) {
        //        Toast.makeText(context, "Invalid inputs", Toast.LENGTH_SHORT).show()
        //    } else {
        //        val mode = if (tempMode == R.id.rb_running) RUNNING else CYCLING
        //        var freqMode : String? = null
        //        when (tempFreq) {
        //            R.id.rb_freq_custom -> freqMode = FREQ_CUSTOM
        //            R.id.rb_freq_everyday -> freqMode = FREQ_EVERYDAY
        //            R.id.rb_freq_once -> freqMode = FREQ_ONCE
        //        }
        //        val mBundle = Bundle()
        //        val schedule = Schedule(mode=mode,frequencyMode=freqMode)
        //        mBundle.putParcelable(EXTRA_SCHEDULE,schedule)
        //        it.findNavController().navigate(
        //            R.id.action_schedulerAddFragment_to_schedulerAddDetailFragment,
        //            mBundle
        //        )
        //    }
        //}
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("test","on activity create")
    }


    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("test","on save instance")
        super.onSaveInstanceState(outState)
        outState.putParcelable(EXTRA_SCHEDULE,schedule)
    }

    override fun onPause() {
        super.onPause()
    }

    private fun setInitialInputValue(savedInstanceState: Bundle?) {
        var mode : String? = null
        var freqMode: String? = null
        savedInstanceState?.let {
            //isRunningMode = it.getBoolean(EXTRA_MODE)
            //freqMode = it.getInt(EXTRA_FREQ)

        }
        //Log.d("test", isRunningMode.toString())

        //isRunningMode?.let {
        //    if (it) binding?.rbRunning?.isChecked = true
        //    else binding?.rbCycling?.isChecked = true
        //}

        //freqMode?.let {
        //    when (it) {
        //        FREQ_CUSTOM -> binding?.rbFreqCustom?.isChecked = true
        //        FREQ_EVERYDAY -> binding?.rbFreqEveryday?.isChecked = true
        //        FREQ_ONCE -> binding?.rbFreqOnce?.isChecked = true
        //    }
        //}
    }
}