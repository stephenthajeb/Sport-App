package com.example.sportapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.sportapp.databinding.FragmentSchedulerAddDetailBinding

class SchedulerAddDetailFragment : Fragment() {
    private var binding: FragmentSchedulerAddDetailBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSchedulerAddDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding?.btnPrev?.setOnClickListener{
        //    //it.findNavController().navigate(R.id.action_schedulerAddDetailFragment_to_schedulerAddFragment)
        //    fragmentManager?.popBackStackImmediate()
        //}

    }

}