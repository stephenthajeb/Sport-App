package com.example.sportapp.UI.Adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sportapp.UI.RecyclingTrackerFragment
import com.example.sportapp.UI.RunningTrackerFragment

class TrainingTrackerFragmentAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position){
            0->fragment = RunningTrackerFragment()
            1->fragment = RecyclingTrackerFragment()
        }
        return fragment as Fragment
    }
}
