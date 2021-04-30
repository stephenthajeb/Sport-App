package com.example.sportapp.UI.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sportapp.Data.News
import com.example.sportapp.Data.Schedule
import com.example.sportapp.R
import com.example.sportapp.databinding.ItemRowNewsBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    private val newsList = ArrayList<News>()
    private lateinit var onItemClickCallback: OnItemClickCallback


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position])
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(newsList[holder.layoutPosition]) }
    }

    fun setNewsData(items:ArrayList<News>){
        newsList.clear()
        newsList.addAll(items)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return newsList.size
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowNewsBinding.bind(itemView)

        fun bind(newsItem: News){
            val source = newsItem.source
            val author = newsItem.author
            var buildSourceWriter: String = ""

            if (author.isNullOrBlank()) {
                buildSourceWriter = "$source, $source"
            } else {
                buildSourceWriter =  "$source, $author"
            }


            val formatter = SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault())
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(newsItem.date)


            with(itemView){
                binding.tvTitle.text = newsItem.title
                binding.tvDate.text = formatter.format(date)
                binding.tvSource.text = buildSourceWriter
                // binding.tvDescription.text = newsItem.description
                // binding.tvAuthor.text = newsItem.author
                Glide.with(this)
                    .load(newsItem.img)
                    .into(binding.ivImg)
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun getNewsList():ArrayList<News> = newsList

    interface OnItemClickCallback {
        fun onItemClicked(data: News)
    }
}