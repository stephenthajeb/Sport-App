package com.example.sportapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.sportapp.databinding.FragmentSchedulerMainBinding


class SchedulerMainFragment : Fragment() {
    private var binding: FragmentSchedulerMainBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSchedulerMainBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnOnClickListener()
    }


    private fun btnOnClickListener(){
        //Alternative 1
        binding?.fabAddNewSchedule?.setOnClickListener{
            it.findNavController().navigate(R.id.action_schedulerMainFragment_to_schedulerAddFragment)
        }

        //Alternative 2
        //binding.fabAddNewSchedule.setOnClickListener(
        //    Navigation.createNavigateOnClickListener(R.id.action_schedulerMainFragment_to_schedulerAddFragment)
        //)
    }


}