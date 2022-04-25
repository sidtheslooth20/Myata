package com.example.musicplayerapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.musicplayerapp.MainActivity
import com.example.musicplayerapp.R
import com.example.musicplayerapp.StreamsViewModel
import com.example.musicplayerapp.databinding.FragmentMainBinding
import com.example.musicplayerapp.service.MediaPlayerService


class MainFragment : Fragment() {

    lateinit var vm: StreamsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        vm = (activity as MainActivity).viewModel

        val binding: FragmentMainBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main, container, false
        )

        vm.currentStreamLive.observe(this, Observer {

            binding.tvMyata.textSize = 16F
            binding.tvGold.textSize = 16F
            binding.tvXtra.textSize = 16F
            when (it) {
                "myata" -> binding.tvMyata.textSize = 20F
                "gold" -> binding.tvGold.textSize = 20F
                "myata_hits" -> binding.tvXtra.textSize = 20F
            }

        })

        vm.currentSongLive.observe(this, Observer {
            binding.mainSong.text = vm.currentSongLive.value
        })

        vm.currentAuthorLive.observe(this, Observer {
            binding.mainAuthor.text = vm.currentAuthorLive.value
        })

        binding.btnPlay.setOnClickListener {
            (activity as MainActivity).startService(
                Intent(
                    context,
                    MediaPlayerService::class.java
                ).also {
                    it.putExtra("DATA", vm.currentStreamLive.value)
                })
            vm.getStreamJson()
        }

        binding.tvGold.setOnClickListener {
            vm.currentStreamLive.value = "gold"
            (activity as MainActivity).startService(
                Intent(
                    context,
                    MediaPlayerService::class.java
                ).also {
                    it.putExtra("DATA", "gold")
                })
        }
        binding.tvXtra.setOnClickListener {
            vm.currentStreamLive.value = "myata_hits"
            (activity as MainActivity).startService(
                Intent(
                    context,
                    MediaPlayerService::class.java
                ).also {
                    it.putExtra("DATA", "myata_hits")
                })
        }
        binding.tvMyata.setOnClickListener {
            vm.currentStreamLive.value = "myata"
            (activity as MainActivity).startService(
                Intent(
                    context,
                    MediaPlayerService::class.java
                ).also {
                    it.putExtra("DATA", "myata")
                })
        }



        return binding.root
    }

}