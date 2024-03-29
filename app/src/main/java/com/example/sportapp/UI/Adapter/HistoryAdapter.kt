package com.example.sportapp.UI.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.sportapp.Data.History
import com.example.sportapp.R
import com.example.sportapp.databinding.ItemRowHistoryBinding
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    private val historyList = MutableLiveData<ArrayList<History>>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: History)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        historyList.value?.get(position)?.let { holder.bind(it) }
        holder.itemView.setOnClickListener {
            historyList.value?.get(holder.layoutPosition)?.let { it1 ->
                onItemClickCallback.onItemClicked(it1)
            }
        }
    }

    fun setData(data: ArrayList<History>) {
        historyList.value = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = historyList.value?.size ?: 0

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowHistoryBinding.bind(itemView)

        fun bind(item: History) {
            val formatter = SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault())
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(item.date)

            val generalDate = formatter.format(date).toString()
            val startDate = item.startTime.toString()
            val finalDate = item.endTime.toString()

            val dist = item.result.toString()
            val ids = item.id.toString()
            val mode = item.mode.toString()

            val buildDesc = "$ids - $mode"
            val buildDescDate = "$generalDate:  $startDate - $finalDate"
            val buildDistance = "Jarak Tempuh: $dist M"

            with(itemView) {
                binding.tvId.text = buildDesc
                binding.tvDescDate.text = buildDescDate
                binding.tvDistance.text = buildDistance

                if (mode != "RUNNING") {
                    binding.ivImg.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_directions_bike_50))
                }
            }
        }
    }
}