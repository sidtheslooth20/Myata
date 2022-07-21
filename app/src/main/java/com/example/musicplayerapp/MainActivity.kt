package com.example.musicplayerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.musicplayerapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: StreamsViewModel
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        val theme = (0..9).random()

        when(theme){
            0->{ setTheme(R.style.AppTheme0) }
            1->{ setTheme(R.style.AppTheme1) }
            2->{ setTheme(R.style.AppTheme2) }
            3->{ setTheme(R.style.AppTheme3) }
            4->{ setTheme(R.style.AppTheme4) }
            5->{ setTheme(R.style.AppTheme5) }
            6->{ setTheme(R.style.AppTheme6) }
            7->{ setTheme(R.style.AppTheme7) }
            8->{ setTheme(R.style.AppTheme8) }
            9->{ setTheme(R.style.AppTheme9) }
        }

        super.onCreate(savedInstanceState)

        val viewModelProviderFactory = StreamsViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(StreamsViewModel ::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        supportActionBar?.hide()
    }
}