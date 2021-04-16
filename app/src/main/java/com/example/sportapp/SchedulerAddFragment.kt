package com.example.sportapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sportapp.databinding.FragmentSchedulerAddBinding
import com.example.sportapp.databinding.FragmentSchedulerMainBinding

class SchedulerAddFragment : Fragment() {
    private var binding: FragmentSchedulerAddBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSchedulerAddBinding.inflate(inflater, container, false)
        return binding?.root
    }

}