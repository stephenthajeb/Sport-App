package com.example.sportapp.UI.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.sportapp.Data.News
import com.example.sportapp.Data.Schedule
import com.example.sportapp.R
import com.example.sportapp.databinding.ItemRowScheduleBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ScheduleAdapter : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    private val scheduleList = MutableLiveData<ArrayList<Schedule>>()
    private var onItemClickCallback: OnItemClickCallback? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_schedule, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        scheduleList.value?.get(position)?.let { holder.bind(it) }

    }

    fun setData(data:ArrayList<Schedule>){
        scheduleList.value = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = scheduleList.value?.size ?: 0

    class ScheduleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowScheduleBinding.bind(itemView)

        fun bind(item: Schedule) {
            val getDays: HashMap<String, String> = hashMapOf(
                "1" to "Mon",
                "2" to "Tue",
                "3" to "Wed",
                "4" to "Thurs",
                "5" to "Fri",
                "6" to "Sat",
                "7" to "Sun"
            )

            val formatter = SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault())
            val mode = item.mode.toString()
            val ids = item.id.toString()
            val freq = item.frequency.toString()
            val startDate = item.startTime.toString()
            val finalDate = item.finishTime.toString()
            val target = item.target.toString()


            with(itemView) {
                if (freq == "ONCE") {
                    val date = formatter.format(
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(item.date)
                    ).toString()
                    val buildDescDate = "$date:  $startDate - $finalDate"
                    binding.tvDescDate.text = buildDescDate
                } else if (freq == "CUSTOM") {
                    val strs = item.days?.split(",")?.toTypedArray()
                    val days = strs?.map { getDays[it] }
                    val DescDate = days.toString().replace("[", "").replace("]", "")
                    val buildDescDate = "Days on: $DescDate $startDate - $finalDate"
                    binding.tvDescDate.text = buildDescDate
                } else if (freq == "EVERYDAY") {
                    val buildDescDate = "Everyday on: $startDate - $finalDate"
                    binding.tvDescDate.text = buildDescDate
                }

                if (item.isAuto == 1) {
                    val buildIds = "$ids - SCHEDULED $freq"
                    binding.tvId.text = buildIds
                } else {
                    val buildIds = "$ids - REMIND ON $freq"
                    binding.tvId.text = buildIds
                }
                binding.tvDistance.text = "Your Goal: $target m"
                if (mode != "RUNNING") {
                    binding.ivImg.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_directions_bike_50))
                }


            }
        }
    }

    interface OnItemClickCallback {
    }


}