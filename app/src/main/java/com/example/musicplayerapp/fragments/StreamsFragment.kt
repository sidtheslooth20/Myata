package com.example.musicplayerapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.musicplayerapp.MainActivity
import com.example.musicplayerapp.R
import com.example.musicplayerapp.StreamsViewModel
import com.example.musicplayerapp.databinding.FragmentStreamsBinding

class StreamsFragment : Fragment() {

    private lateinit var vm: StreamsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentStreamsBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_streams, container,false)

        vm = (activity as MainActivity).viewModel
        binding.viewmodel = vm


        binding.myataStreamBanner.setOnClickListener {
            vm.currentStreamLive.value = "myata"
            findNavController().navigate(R.id.player_myata)
        }

        binding.goldStreamBanner.setOnClickListener {
            vm.currentStreamLive.value = "gold"
            findNavController().navigate(R.id.player_myata)
        }

        binding.xtraStreamBanner.setOnClickListener {
            vm.currentStreamLive.value = "myata_hits"
            findNavController().navigate(R.id.player_myata)
        }

        binding.homeBtn.setOnClickListener {
            findNavController().navigate(R.id.mainFragment)
        }

        binding.playerBtn.setOnClickListener {
            findNavController().navigate(R.id.player_myata)
        }

        return binding.root
    }
}