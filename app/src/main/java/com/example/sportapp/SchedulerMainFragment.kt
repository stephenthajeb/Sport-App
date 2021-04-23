package com.example.sportapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.sportapp.databinding.FragmentSchedulerMainBinding


class SchedulerMainFragment : Fragment() {
    private var binding: FragmentSchedulerMainBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSchedulerMainBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnOnClickListener()
    }


    private fun btnOnClickListener(){
        binding?.fabAddNewSchedule?.setOnClickListener{
            //Todo: change to using navgraph
            val intent = Intent(context,SchedulerAddActivity::class.java)
            startActivity(intent)
        }
    }


}