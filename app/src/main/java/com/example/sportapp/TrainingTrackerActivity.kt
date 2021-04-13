package com.example.sportapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.sportapp.databinding.ActivityTrainingTrackerBinding

class TrainingTrackerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrainingTrackerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainingTrackerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setActiveNavMenu()
        menuItemListener()
    }


    private fun setActiveNavMenu(){
        val menuItem : MenuItem = binding.bottomNavView.menu.getItem(1)
        menuItem.isChecked = true
    }

    private fun menuItemListener(){
        binding.bottomNavView.setOnNavigationItemSelectedListener {item->
            when(item.itemId){
                R.id.nav_news->{
                    val intent = Intent(this,NewsActivity::class.java )
                    startActivity(intent)
                }
                R.id.nav_tracker->{
                    return@setOnNavigationItemSelectedListener true
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }
}