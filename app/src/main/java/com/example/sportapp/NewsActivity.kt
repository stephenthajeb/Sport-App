package com.example.sportapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.example.sportapp.databinding.ActivityMainBinding
import com.example.sportapp.databinding.ActivityTrainingTrackerBinding

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setActiveNavMenu()
        menuItemListener()
    }

    private fun setActiveNavMenu(){
        val menuItem : MenuItem = binding.bottomNavView.menu.getItem(0)
        menuItem.isChecked = true
    }

    private fun menuItemListener(){
        binding.bottomNavView.setOnNavigationItemSelectedListener {item->
            when(item.itemId){
                R.id.nav_news->{
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_tracker->{
                    val intent = Intent(this,TrainingTrackerActivity::class.java )
                    startActivity(intent)
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }
}