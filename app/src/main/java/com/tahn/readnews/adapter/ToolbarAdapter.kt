package com.tahn.readnews.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tahn.readnews.R
import com.tahn.readnews.data.ToolbarTitle

class ToolbarAdapter(private val listTitle : List<ToolbarTitle>, private val listener : OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var currentSelected : ToolbarTitle = listTitle[0]
    private var previousSelected : ToolbarTitle? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_toolbar_item, parent, false)
        return ToolbarViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listTitle.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ToolbarViewHolder).bind(listTitle[position], position)
    }

    inner class ToolbarViewHolder(view : View) : RecyclerView.ViewHolder(view){
        private val title : TextView = view.findViewById(R.id.title_toolbar)
        private val highLight : View = view.findViewById(R.id.highlight)

        fun bind(toolbarTitle: ToolbarTitle, position: Int){
            this.title.text = toolbarTitle.title

            if(toolbarTitle.check){
                highLight.visibility = View.VISIBLE
            }else{
                highLight.visibility = View.INVISIBLE
            }

            itemView.setOnClickListener {
                previousSelected = currentSelected
                currentSelected = toolbarTitle

                previousSelected?.check = false
                currentSelected.check = true

                listener.onClick(toolbarTitle)
                notifyDataSetChanged()
            }
        }
    }
}

interface OnItemClickListener{
    fun onClick(title : ToolbarTitle)
}