package com.example.sportapp.UI

import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportapp.Data.History
import com.example.sportapp.HistoryModelFactory
import com.example.sportapp.HistoryViewModel
import com.example.sportapp.R
import com.example.sportapp.SportApp
import com.example.sportapp.UI.Adapter.HistoryAdapter
import com.example.sportapp.databinding.FragmentHistoryDetailBinding
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat


class HistoryDetailFragment : Fragment() {
    private var adapter: HistoryAdapter? = null
    private var binding: FragmentHistoryDetailBinding? = null
    private val historyViewModel: HistoryViewModel by viewModels{
        HistoryModelFactory((activity?.application as SportApp).historyDAO)
    }

    companion object{
        const val EXTRA_HISTORY = "history"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryDetailBinding.inflate(layoutInflater)
        setUpAdapter()
        return binding?.root
    }

    private fun setUpAdapter(){
        adapter = HistoryAdapter()
        adapter?.setOnItemClickCallback(object : HistoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: History) {
                val orientation = resources.configuration.orientation
                val bundle = Bundle()
                if(data.mode == SchedulerAddActivity.CYCLING){
                    val datas = History(
                            mode = data.mode,
                            startTime = data.startTime,
                            date = data.date,
                            endTime = data.endTime,
                            result = data.result
                    )
                    bundle.putParcelable(EXTRA_HISTORY,datas)
                    val outputStream = ByteArrayOutputStream()
                    data.img?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    val byteArray: ByteArray = outputStream.toByteArray()
                    bundle.putByteArray("Bitmap", byteArray)
                }
                else{
                    bundle.putParcelable(EXTRA_HISTORY,data)
                }
                if (orientation == Configuration.ORIENTATION_LANDSCAPE){
                    //Navigate to fragment
                    findNavController().navigate(R.id.action_historyDetailFragment2_to_historyDetailTrainingFragment2,bundle)
                } else {
                    //Navigate to another activity
                    findNavController().navigate(R.id.action_historyDetailFragment2_to_historyDetailTrainingActivity,bundle)
                }
            }
        })
        val activity = requireActivity()
        binding?.rvHistory?.layoutManager = LinearLayoutManager(context)
        binding?.rvHistory?.adapter = adapter
        val formattedDate = SimpleDateFormat("yyyy-MM-dd").format(activity.intent.getSerializableExtra(HistoryActivity.EXTRA_CALENDAR) )
        historyViewModel.getHistoriesOnDate(formattedDate).observe(activity){
            adapter?.setData(it as ArrayList<History>)
        }

    }

}