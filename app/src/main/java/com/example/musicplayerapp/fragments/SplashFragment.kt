package com.example.musicplayerapp.fragments


import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.musicplayerapp.MainActivity
import com.example.musicplayerapp.R
import com.example.musicplayerapp.StreamsViewModel
import com.example.musicplayerapp.databinding.FragmentSplashBinding
import okhttp3.internal.wait


class SplashFragment : Fragment() {

    lateinit var vm: StreamsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSplashBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_splash, container, false
        )

        vm = (activity as MainActivity).viewModel

        vm.playlistList.observe(viewLifecycleOwner, Observer {
            Log.e("OBSRVR","trigger")
            if(it.size!=0)
                findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
        })

        return binding.root
    }

}