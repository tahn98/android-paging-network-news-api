package com.tahn.readnews.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tahn.readnews.R
import com.tahn.readnews.data.NetworkState
import com.tahn.readnews.data.Status

class LoadingViewHolder(
    view : View,
    private val retryCallback : () -> Unit
) : RecyclerView.ViewHolder(view){

    private val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
    private val retry = view.findViewById<Button>(R.id.retry_button)
    private val errorMsg = view.findViewById<TextView>(R.id.error_msg)

    init {
        retry.setOnClickListener {
            retryCallback()
        }
    }

    fun bind(networkState: NetworkState?) {
        progressBar.visibility = toVisibility(networkState?.status == Status.RUNNING)
        retry.visibility = toVisibility(networkState?.status == Status.FAILED)
        errorMsg.visibility = toVisibility(networkState?.msg != null)
        errorMsg.text = networkState?.msg
    }


    companion object{
        fun create(parent: ViewGroup, retryCallback: () -> Unit): LoadingViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_loading, parent, false)
            return LoadingViewHolder(view, retryCallback)
        }

        fun toVisibility(constraint : Boolean): Int {
            return if (constraint) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
}