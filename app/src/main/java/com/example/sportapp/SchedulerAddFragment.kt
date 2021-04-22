package com.example.sportapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.sportapp.databinding.FragmentSchedulerAddBinding


class SchedulerAddFragment : Fragment(), DatePickerFragment.DialogDateListener, TimePickerFragment.DialogTimeListener{
    private var binding: FragmentSchedulerAddBinding? = null
    private var schedule: Schedule? = null

    companion object {
        const val RUNNING = "RUNNING"
        const val CYCLING = "CYCLING"
        const val FREQ_ONCE = "ONCE"
        const val FREQ_EVERYDAY = "EVERYDAY"
        const val FREQ_CUSTOM = "CUSTOM"
        private const val DATE_PICKER_TAG = "DatePicker"
        private const val TIME_PICKER_START_TAG = "TimePickerStart"
        private const val TIME_PICKER_END_TAG = "TimePickerEnd"
        const val EXTRA_SCHEDULE = "Schedule"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSchedulerAddBinding.inflate(inflater, container, false)
        setInitialInputValue(savedInstanceState)
        setUpBtnListener()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Log.d("test","on view created")

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EXTRA_SCHEDULE, schedule)
    }

    private fun setUpBtnListener(){
        val binding = binding!!
        binding.btnDatePicker.setOnClickListener{
            val datePicker = DatePickerFragment()
            activity?.supportFragmentManager?.let { it1 -> datePicker.show(it1, DATE_PICKER_TAG) }
        }
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        TODO("Not yet implemented")
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        TODO("Not yet implemented")
    }

    private fun setInitialInputValue(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            schedule = it.getParcelable(EXTRA_SCHEDULE)
        }
        //Todo: show in UI
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