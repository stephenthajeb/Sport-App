package com.example.sportapp.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sportapp.databinding.FragmentHistoryDetailTrainingBinding

class HistoryDetailTrainingFragment : Fragment() {
    private var binding: FragmentHistoryDetailTrainingBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryDetailTrainingBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding?.root
    }

}