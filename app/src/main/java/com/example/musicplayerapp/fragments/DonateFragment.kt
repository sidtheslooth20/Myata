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
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.musicplayerapp.R
import com.example.musicplayerapp.databinding.FragmentDonateBinding


class DonateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentDonateBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_donate, container, false
        )

        binding.summ.addTextChangedListener {
            binding.summ.background.setColorFilter(Color.CYAN,PorterDuff.Mode.MULTIPLY)
        }

        binding.sendBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.setData(Uri.parse("https://music.yandex.ru/users/shaldin.voice/playlists/"))
            startActivity(intent)
        }

        binding.homeBtn.setOnClickListener {
            findNavController().navigate(R.id.mainFragment)
        }

        binding.playerBtn.setOnClickListener {
            findNavController().navigate(R.id.player_myata)
        }

        binding.infoBtn.setOnClickListener {
            findNavController().navigate(R.id.infoFragment)
        }

        return binding.root
    }
}