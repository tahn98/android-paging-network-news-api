package com.tahn.readnews.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tahn.readnews.R
import com.tahn.readnews.data.Article

class ArticleItemViewHolder(view : View): RecyclerView.ViewHolder(view){

    private val name : TextView = view.findViewById(R.id.article_name)

    fun bind(item : Article?){
        name.text = item?.title.toString()
    }

    companion object{
        fun create(parent : ViewGroup) : ArticleItemViewHolder{
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_article_item, parent, false)
            return ArticleItemViewHolder(view)
        }
    }
}