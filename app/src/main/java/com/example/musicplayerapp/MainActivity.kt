package com.example.musicplayerapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.musicplayerapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: StreamsViewModel

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val viewModelProviderFactory = StreamsViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(StreamsViewModel ::class.java)

        binding.bottomNavigationView.setupWithNavController(findNavController(R.id.navHostFragment))
        supportActionBar?.hide()
    }


    fun showBottomNavView(){
        binding.bottomNavigationView.visibility = View.VISIBLE
    }
}