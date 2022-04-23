package com.example.musicplayerapp.fragments

import android.media.AudioAttributes
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.musicplayerapp.MainActivity
import com.example.musicplayerapp.R
import com.example.musicplayerapp.StreamsViewModel
import com.example.musicplayerapp.databinding.FragmentMainBinding
import com.example.musicplayerapp.databinding.FragmentStreamsBinding
import org.w3c.dom.Text


class MainFragment : Fragment() {

    lateinit var vm: StreamsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        vm = (activity as MainActivity).viewModel

        val binding: FragmentMainBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_main, container,false)

        binding.viewmodel = vm

        binding.btnPlay.setOnClickListener {

            vm.play_stream()
//            val metaRetriever = MediaMetadataRetriever()
//            metaRetriever.setDataSource(context, Uri.parse("https://radio-node-6.dline-media.com/myata"))
//
//            val artist: String? =  metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
//            val song: String? = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
//
//            vm.currentSongLive.value = song
//            vm.currentAuthorLive.value = artist
        }


        return binding.root
    }

}