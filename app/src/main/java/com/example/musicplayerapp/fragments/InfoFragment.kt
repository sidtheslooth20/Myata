package com.example.musicplayerapp.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.musicplayerapp.R
import com.example.musicplayerapp.databinding.FragmentInfoBinding

class InfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentInfoBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_info, container, false
        )

        binding.yandex.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.setData(Uri.parse("https://music.yandex.ru/users/shaldin.voice/playlists/"))
            startActivity(intent)
        }
        binding.instagram.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.setData(Uri.parse("https://www.instagram.com/radiomyata/"))
            startActivity(intent)
        }
        binding.boosty.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.setData(Uri.parse("https://boosty.to/myata"))
            startActivity(intent)
        }
        binding.vk.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.setData(Uri.parse("https://vk.com/radiomyata"))
            startActivity(intent)
        }
        binding.youtube.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.setData(Uri.parse("https://www.youtube.com/channel/UC30ExLCP-enuCrHH2qRRlCw"))
            startActivity(intent)
        }
        binding.twitter.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.setData(Uri.parse("https://twitter.com/radiomyata"))
            startActivity(intent)
        }
        binding.telegram.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.setData(Uri.parse("https://t.me/radiomyata"))
            startActivity(intent)
        }
        binding.spotify.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.setData(Uri.parse("https://open.spotify.com/user/31b7rfatuqf7lc76thiudm5bxxuy"))
            startActivity(intent)
        }


        binding.playerBtn.setOnClickListener {
            findNavController().navigate(R.id.player_myata)
        }
        binding.donateBtn.setOnClickListener {
            findNavController().navigate(R.id.donate)
        }
        binding.homeBtn.setOnClickListener {
            findNavController().navigate(R.id.mainFragment)
        }

        return binding.root
    }
}