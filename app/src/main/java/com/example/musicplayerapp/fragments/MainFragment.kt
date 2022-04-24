package com.example.musicplayerapp.fragments

import android.content.Intent
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
import androidx.lifecycle.Observer
import com.example.musicplayerapp.MainActivity
import com.example.musicplayerapp.R
import com.example.musicplayerapp.StreamsViewModel
import com.example.musicplayerapp.databinding.FragmentMainBinding
import com.example.musicplayerapp.databinding.FragmentStreamsBinding
import com.example.musicplayerapp.service.MediaPlayerService
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

        vm.currentStream.observe(this, Observer {

            binding.tvMyata.textSize = 16F
            binding.tvGold.textSize = 16F
            binding.tvXtra.textSize = 16F
            when(it){
                "myata"->binding.tvMyata.textSize = 20F
                "gold"->binding.tvGold.textSize = 20F
                "myata_hits"->binding.tvXtra.textSize = 20F
            }

        })

        binding.btnPlay.setOnClickListener {
             (activity as MainActivity).startService(Intent(context,MediaPlayerService::class.java).also{
                 it.putExtra("DATA", vm.currentStream.value)
             })
        }

        binding.tvGold.setOnClickListener {
            vm.currentStream.value = "gold"
            (activity as MainActivity).startService(Intent(context,MediaPlayerService::class.java).also{
                it.putExtra("DATA", "gold")
            })
        }
        binding.tvXtra.setOnClickListener {
            vm.currentStream.value = "myata_hits"
            (activity as MainActivity).startService(Intent(context,MediaPlayerService::class.java).also{
                it.putExtra("DATA", "myata_hits")
            })
        }
        binding.tvMyata.setOnClickListener {
            vm.currentStream.value = "myata"
            (activity as MainActivity).startService(Intent(context,MediaPlayerService::class.java).also{
                it.putExtra("DATA", "myata")
            })
        }



        return binding.root
    }

}