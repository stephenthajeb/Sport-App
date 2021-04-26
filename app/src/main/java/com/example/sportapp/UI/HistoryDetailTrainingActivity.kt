package com.example.sportapp.UI

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.sportapp.R
import com.example.sportapp.databinding.ActivityHistoryDetailTrainingBinding

class HistoryDetailTrainingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryDetailTrainingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryDetailTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}