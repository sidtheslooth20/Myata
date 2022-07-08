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
import androidx.lifecycle.Observer
import com.example.musicplayerapp.MainActivity
import com.example.musicplayerapp.service.MediaPlayerService
import com.example.musicplayerapp.R
import com.example.musicplayerapp.StreamsViewModel
import com.example.musicplayerapp.databinding.FragmentMainBinding
import com.example.musicplayerapp.databinding.FragmentPlayerBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.IOException


class PlayerFragment : Fragment() {

    lateinit var vm: StreamsViewModel
    lateinit var binding: FragmentPlayerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        vm = (activity as MainActivity).viewModel

        vm.getStreamJson()

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_player, container, false
        )

        vm.currentStreamLive.observe(viewLifecycleOwner, Observer {

            binding.tvMyata.textSize = 16F
            binding.tvGold.textSize = 16F
            binding.tvXtra.textSize = 16F
            when (it) {
                "myata" -> binding.tvMyata.textSize = 20F
                "gold" -> binding.tvGold.textSize = 20F
                "myata_hits" -> binding.tvXtra.textSize = 20F
            }
            (activity as MainActivity).startService(
                Intent(
                    context,
                    MediaPlayerService::class.java
                ).also {
                    it.putExtra("STREAM", vm.currentStreamLive.value)
                    it.putExtra("ACTION", "switch")
                })

        })

        vm.currentImgLinkLive.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Picasso.get().load(Uri.parse(it.replace("avatar170", "avatar770"))).into(binding.photo)
            }
        })

        vm.currentSongLive.observe(viewLifecycleOwner, Observer {
            binding.mainSong.text = vm.currentSongLive.value
        })

        vm.currentAuthorLive.observe(viewLifecycleOwner, Observer {
            binding.mainAuthor.text = vm.currentAuthorLive.value
            if(it!=null){
                GlobalScope.launch {
                    getImage()
                }
            }
        })

        binding.btnPlay.setOnClickListener {
            (activity as MainActivity).startService(
                Intent(
                    context,
                    MediaPlayerService::class.java
                ).also {
                    it.putExtra("STREAM", vm.currentStreamLive.value)
                    it.putExtra("ACTION", "startStop")
                })
        }

        binding.tvGold.setOnClickListener {
            vm.currentStreamLive.value = "gold"
        }
        binding.tvXtra.setOnClickListener {
            vm.currentStreamLive.value = "myata_hits"
        }
        binding.tvMyata.setOnClickListener {
            vm.currentStreamLive.value = "myata"
        }



        return binding.root
    }

    fun getImage() {
        try {
            val doc: org.jsoup.nodes.Document = Jsoup.connect(
                "https://last.fm/music/${
                    vm.currentAuthorLive.value
                        ?.trim()?.replace(" ", "+")
                }/+images"
            ).get()

            val elements: Elements = doc.select("ul[class=image-list]")
            vm.currentImgLinkLive.postValue(elements.select("a[class=image-list-item]")
                .select("img").attr("src"))

        } catch (e:IOException){
            Log.e("IOException", "smth wrong with image parse")
        }
    }

}