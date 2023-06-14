package com.example.database

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(private val eventList : ArrayList<DataEvent>) : RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = eventList[position]
        holder.title.text = currentItem.title
        holder.description.text = currentItem.description
        holder.room.text = currentItem.room
        holder.date.text = currentItem.date
        holder.time.text = currentItem.time
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val title : TextView = itemView.findViewById(R.id.titleTextView)
        val description : TextView = itemView.findViewById(R.id.descriptionTextView)
        val room : TextView = itemView.findViewById(R.id.roomTextView)
        val date : TextView = itemView.findViewById(R.id.dateTextView)
        val time : TextView = itemView.findViewById(R.id.timeTextView)
    }

}