package com.example.sportapp.UI.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sportapp.Data.News
import com.example.sportapp.R
import com.example.sportapp.databinding.ItemRowNewsBinding

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
            with(itemView){
                binding.tvTitle.text = newsItem.title
                binding.tvDate.text = newsItem.date
                binding.tvSource.text = newsItem.source
                binding.tvDescription.text = newsItem.description
                binding.tvAuthor.text = newsItem.author
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