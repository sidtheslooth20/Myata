package com.example.musicplayerapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.musicplayerapp.MainActivity
import com.example.musicplayerapp.R
import com.example.musicplayerapp.StreamsViewModel
import com.example.musicplayerapp.StreamsViewModelFactory
import com.example.musicplayerapp.databinding.FragmentStreamsBinding

class StreamsFragment : Fragment() {

    private lateinit var vm: StreamsViewModel

    lateinit var songLayoutParameters : LinearLayout.LayoutParams

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentStreamsBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_streams, container,false)

        songLayoutParameters = binding.streamsCurrentSong.layoutParams as LinearLayout.LayoutParams


        vm = (activity as MainActivity).viewModel
        songLayoutParameters.weight = vm.songLayoutWeight
        binding.viewmodel = vm

        vm.currentSongLive.observe(this, Observer {
            if(it == null){
                songLayoutParameters.weight = vm.setSongLayoutWeight(0F)
            }
            else{
                songLayoutParameters.weight = vm.setSongLayoutWeight(2F)
            }
        })

        vm.currentSongLive.value = "MIKE SHINODA FT. UPSAHL"


        binding.streamGoldHits.setOnClickListener {
            vm.currentStream = "gold"
            findNavController().navigate(R.id.mainFragment)
        }

        binding.streamXtra.setOnClickListener {
            vm.currentStream = "myata_hits"
            findNavController().navigate(R.id.mainFragment)
        }

        binding.streamMyata.setOnClickListener {
            vm.currentStream = "myata"
            findNavController().navigate(R.id.mainFragment)
        }

        return binding.root
    }
}