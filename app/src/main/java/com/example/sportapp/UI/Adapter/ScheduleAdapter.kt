package com.example.sportapp.UI.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.sportapp.Data.Schedule
import com.example.sportapp.R
import com.example.sportapp.databinding.ItemRowScheduleBinding

class ScheduleAdapter : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {
    //private val scheduleList = ArrayList<Schedule>()
    private val scheduleList = MutableLiveData<ArrayList<Schedule>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_schedule, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        scheduleList?.value?.get(position)?.let { holder.bind(it) }
    }

    fun setData(data:ArrayList<Schedule>){
        scheduleList.value = data
    }

    fun getData(): ArrayList<Schedule>? = scheduleList.value

    override fun getItemCount(): Int = scheduleList?.value?.size ?: 0

    class ScheduleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowScheduleBinding.bind(itemView)

        fun bind(item: Schedule){
            with(itemView){
                binding.tvId.text = item.id.toString()
                binding.tvFreq.text = item.frequency
            }
        }
    }



}