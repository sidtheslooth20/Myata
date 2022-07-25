package com.example.musicplayerapp.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.musicplayerapp.MainActivity
import com.example.musicplayerapp.R
import com.example.musicplayerapp.StreamsViewModel
import com.example.musicplayerapp.databinding.FragmentDonateBinding


class DonateFragment : Fragment() {

    lateinit var vm: StreamsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding: FragmentDonateBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_donate, container, false
        )

        vm = (activity as MainActivity).viewModel

        binding.webView.apply {
            this.settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl(getString(R.string.donate_url))
        }

        binding.homeBtn.setOnClickListener {
            findNavController().navigate(R.id.mainFragment)
        }

        binding.playerBtn.setOnClickListener {
            findNavController().navigate(R.id.playerFragment, Bundle().apply {
                when(vm.currentStreamLive.value){
                    "myata"->putInt(CURRENT_ITEM, 0)
                    "gold"->putInt(CURRENT_ITEM, 1)
                    "myata_hits"->putInt(CURRENT_ITEM, 2)
                }
            })
        }

        binding.infoBtn.setOnClickListener {
            findNavController().navigate(R.id.infoFragment)
        }

        return binding.root
    }
}