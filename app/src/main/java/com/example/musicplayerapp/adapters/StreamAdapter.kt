package com.example.musicplayerapp.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayerapp.R


class StreamAdapter(private val streams: List<Int>):
    RecyclerView.Adapter<StreamAdapter.StreamViewHolder>() {

    class StreamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.findViewById(R.id.bgImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rw_stream_item,parent,false)
        return StreamViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StreamViewHolder, position: Int) {
        holder.imageView.setImageResource(streams[position])
    }

    override fun getItemCount(): Int {
        return 3
    }

}