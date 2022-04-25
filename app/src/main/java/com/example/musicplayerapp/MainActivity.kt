package com.example.musicplayerapp

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.musicplayerapp.databinding.ActivityMainBinding
import com.example.musicplayerapp.service.MediaPlayerService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {


    lateinit var viewModel: StreamsViewModel
    lateinit var binding: ActivityMainBinding

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
        }
        override fun onServiceDisconnected(arg0: ComponentName) {
        }
    }

    suspend fun createServiceAsync() = withContext(Dispatchers.IO){
        createService()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val viewModelProviderFactory = StreamsViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(StreamsViewModel ::class.java)

        binding.bottomNavigationView.setupWithNavController(findNavController(R.id.navHostFragment))
        supportActionBar?.hide()
    }

    fun createService(){
        Intent(this, MediaPlayerService::class.java).also {
            bindService(it, connection, BIND_AUTO_CREATE)
        }
    }

    fun showBottomNavView(){
        binding.bottomNavigationView.visibility = View.VISIBLE
    }
}