package com.example.sportapp.UI

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sportapp.Data.History
import com.example.sportapp.R
import com.example.sportapp.databinding.ActivityHistoryDetailTrainingBinding
import com.example.sportapp.databinding.FragmentHistoryDetailBinding
import com.example.sportapp.databinding.FragmentHistoryDetailTrainingBinding
import java.text.SimpleDateFormat
import java.util.*

class HistoryDetailTrainingFragment : Fragment() {
    private var binding: FragmentHistoryDetailTrainingBinding? = null

    companion object{
        var history: History?= null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "History Detail Fragment"
        Toast.makeText(context,"History Detail Fragment",Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryDetailTrainingBinding.inflate(layoutInflater)
        HistoryDetailTrainingActivity.history = this.arguments?.getParcelable(HistoryDetailFragment.EXTRA_HISTORY)
        val formatter = SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault())
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(HistoryDetailTrainingActivity.history?.date)
        val buildDate = formatter.format(date).toString()
        val endTime = HistoryDetailTrainingActivity.history?.endTime
        val startTime = HistoryDetailTrainingActivity.history?.startTime

        binding!!.dateDetail.text = "Your training on $buildDate"
        binding!!.endDate.text = "Your training end on $endTime"
        binding!!.startDate.text  = "Your training start on $startTime"
        var bytes : ByteArray? = this.arguments?.getByteArray("Bitmap")
        if(bytes!=null){
            binding!!.imageHistory.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
        }
        val result = HistoryDetailTrainingActivity.history?.result
        binding!!.mode.text = HistoryDetailTrainingActivity.history?.mode
        binding!!.result.text = "Your distance $result m"
        if(HistoryDetailTrainingActivity.history?.mode == SchedulerAddActivity.RUNNING){
            binding!!.logoMode.setImageResource(R.drawable.ic_baseline_directions_run_50)
        }
        else{
            binding!!.logoMode.setImageResource(R.drawable.ic_baseline_directions_bike_50)
        }
        return binding?.root
    }

}