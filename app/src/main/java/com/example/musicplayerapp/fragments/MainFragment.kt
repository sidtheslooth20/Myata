package com.example.musicplayerapp.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayerapp.MainActivity
import com.example.musicplayerapp.R
import com.example.musicplayerapp.StreamsViewModel
import com.example.musicplayerapp.adapters.PlaylistAdapter
import com.example.musicplayerapp.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding
    lateinit var vm: StreamsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        vm = (activity as MainActivity).viewModel

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main, container, false
        )
        binding.playlists.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.playlists.adapter = vm.playlistList.value?.let { PlaylistAdapter(it, { position -> onItemClick(position)}) }


        binding.infoBtn.setOnClickListener {
            findNavController().navigate(R.id.infoFragment)
        }

        binding.myataStreamBanner.setOnClickListener {
            vm.currentStreamLive.value = "myata"
            findNavController().navigate(R.id.playerFragment, Bundle().apply {
                putInt(CURRENT_ITEM, 0)
            })
        }

        binding.goldStreamBanner.setOnClickListener {
            vm.currentStreamLive.value = "gold"
            findNavController().navigate(R.id.playerFragment, Bundle().apply {
                putInt(CURRENT_ITEM, 1)
            })
        }

        binding.xtraStreamBanner.setOnClickListener {
            vm.currentStreamLive.value = "myata_hits"
            findNavController().navigate(R.id.playerFragment, Bundle().apply {
                putInt(CURRENT_ITEM, 2)
            })
        }

        binding.donateBtn.setOnClickListener {
            findNavController().navigate(R.id.donate)
        }

        // Inflate the layout for this fragment
        return binding.root
    }


    private fun onItemClick(position: Int){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        intent.setData(Uri.parse(vm.playlistList.value!![position].uri))
        startActivity(intent)
    }

}