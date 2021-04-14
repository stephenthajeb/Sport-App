package com.example.sportapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.example.sportapp.databinding.ActivityTrainingTrackerBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TrainingTrackerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrainingTrackerBinding

    companion object{
        private var TAB_TITLES = arrayOf(
            TrainingTrackerFragment.RUNNING_MODE,
            TrainingTrackerFragment.CYCLING_MODE,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainingTrackerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setActiveNavMenu()
        menuItemListener()
        setUpTabLayout()
    }

    private fun setUpTabLayout(){
        val tabLayoutAdapter = TrainingTrackerFragmentAdapter(this)
        binding.viewPager.adapter = tabLayoutAdapter
        TabLayoutMediator(binding.tabs,binding.viewPager){ tab,position->
            tab.text = TAB_TITLES[position]
        }.attach()
        supportActionBar?.elevation = 0f
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