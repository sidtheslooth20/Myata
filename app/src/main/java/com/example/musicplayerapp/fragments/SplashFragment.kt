package com.example.musicplayerapp.fragments

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.musicplayerapp.MainActivity
import com.example.musicplayerapp.R
import com.example.musicplayerapp.StreamsViewModel
import com.example.musicplayerapp.databinding.ActivityMainBinding
import com.example.musicplayerapp.databinding.FragmentStreamsBinding
import com.example.musicplayerapp.service.MediaPlayerService

class SplashFragment : Fragment() {

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
        }
        override fun onServiceDisconnected(arg0: ComponentName) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Handler().postDelayed(
            {
                (requireActivity() as MainActivity).showBottomNavView()
                findNavController().navigate(R.id.action_splashFragment_to_streamsFragment)
            }, 2000)

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createService()
    }

    fun createService(){
        Intent(activity as MainActivity, MediaPlayerService::class.java).also {
            (activity as MainActivity).bindService(it, connection, AppCompatActivity.BIND_AUTO_CREATE)
        }
    }

}