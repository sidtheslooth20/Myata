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
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.musicplayerapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {


    lateinit var viewModel: StreamsViewModel
    lateinit var binding: ActivityMainBinding


//    private val connection = object : ServiceConnection {
//        override fun onServiceConnected(className: ComponentName, service: IBinder) {
//        }
//        override fun onServiceDisconnected(arg0: ComponentName) {
//        }
//    }
//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val viewModelProviderFactory = StreamsViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(StreamsViewModel ::class.java)

        //binding.bottomNavigationView.setupWithNavController(findNavController(R.id.navHostFragment))
        supportActionBar?.hide()
    }

    fun showBottomNavView(){
        //binding.bottomNavigationView.visibility = View.VISIBLE
    }
//
//    suspend fun createService() = withContext(Dispatchers.IO){
//        Intent(this@MainActivity, MediaPlayerService::class.java).also {
//            (this@MainActivity.bindService(it, connection, AppCompatActivity.BIND_AUTO_CREATE))
//        }
//    }
}