package com.example.musicplayerapp.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayerapp.R
import com.squareup.picasso.Picasso


class PlaylistAdapter(private val playlists: List<Uri>, private val names: List<String>):
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.findViewById(R.id.playlistBgImage)
        val textView: TextView = itemView.findViewById(R.id.playlistText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rw_stream_item,parent,false)
        return PlaylistViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        Picasso.get().load(playlists[position]).into(holder.imageView)
        holder.textView.text = names[position]
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

}