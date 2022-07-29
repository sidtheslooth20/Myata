package com.example.musicplayerapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.musicplayerapp.MainActivity
import com.example.musicplayerapp.R
import com.example.musicplayerapp.StreamsViewModel
import com.example.musicplayerapp.adapters.FragmentStreamAdapter
import com.example.musicplayerapp.databinding.FragmentPlayerBinding
import com.example.musicplayerapp.service.MediaPlayerService

const val CURRENT_ITEM = "0"

class PlayerFragment : Fragment() {

    lateinit var binding:FragmentPlayerBinding
    lateinit var vm: StreamsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        vm = (activity as MainActivity).viewModel

        val adapter = FragmentStreamAdapter(this)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_player, container, false
        )

        binding.viewPager.adapter = adapter


        arguments?.takeIf { it.containsKey(CURRENT_ITEM) }?.apply {
            binding.viewPager.post {
                binding.viewPager.setCurrentItem(getInt(CURRENT_ITEM), true)
            }
        }

        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                when(position){
                    0->vm.currentStreamLive.value = "myata"
                    1->vm.currentStreamLive.value = "gold"
                    2->vm.currentStreamLive.value = "myata_hits"
                }
                super.onPageSelected(position)
            }
        })

        return binding.root
    }

    override fun onDestroy() {
        (activity as MainActivity).stopService(Intent(context, MediaPlayerService::class.java))
        super.onDestroy()
    }

}