package com.example.musicplayerapp.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.musicplayerapp.MainActivity
import com.example.musicplayerapp.R
import com.example.musicplayerapp.StreamsViewModel
import com.example.musicplayerapp.databinding.FragmentPlayerBinding
import com.example.musicplayerapp.service.MediaPlayerService
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation


class PlayerFragment : Fragment() {

    lateinit var vm: StreamsViewModel
    lateinit var binding: FragmentPlayerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm = (activity as MainActivity).viewModel

        if(vm.currentAuthorLive.value == null)
            vm.getStreamJson()

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_player, container, false
        )

        updatePlayer()

        vm.currentStreamLive.observe(viewLifecycleOwner, Observer {
            updatePlayer()
            when(it){
                "myata"->binding.constraintLayout.setBackgroundResource(R.drawable.myata_bg)
                "gold"->binding.constraintLayout.setBackgroundResource(R.drawable.gold_bg)
                "myata_hits"->binding.constraintLayout.setBackgroundResource(R.drawable.xtra_bg)
            }
        })

        //Too much observers, might want to create class with (author, song, image)
        vm.currentImgLinkLive.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Picasso.get().load(Uri.parse(it.replace("avatar170", "avatar770"))).transform(CropCircleTransformation()).resize(800,800).centerCrop().into(binding.photo)
            }
            else
                binding.logo.setImageResource(R.drawable.logo)
        })

        vm.currentSongLive.observe(viewLifecycleOwner, Observer {
            binding.mainSong.text = vm.currentSongLive.value
            (activity as MainActivity).startService(
                Intent(
                    context,
                    MediaPlayerService::class.java
                ).also {
                    it.putExtra("ACTION", "switch_track")
                    if(vm.currentAuthorLive.value != null && vm.currentSongLive.value!= null) {
                        it.putExtra("SONG", vm.currentSongLive.value)
                        it.putExtra("ARTIST", vm.currentAuthorLive.value)
                    }
                    else{
                        it.putExtra("SONG", "You are listening to")
                        it.putExtra("ARTIST", "Radio Myata")
                    }
                })
        })

        vm.currentAuthorLive.observe(viewLifecycleOwner, Observer {
            if(it!=null && !it.isBlank()){
                binding.mainAuthor.text = vm.currentAuthorLive.value
                vm.getImage()
            }
            else{
                binding.mainAuthor.text = "YOU ARE LISTENING TO RADIO MYATA"
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
            if(!vm.isPlaying) {
                binding.btnPlay.setImageResource(R.drawable.pause_btn)

            }
            else {
                binding.btnPlay.setImageResource(R.drawable.btn_play)
                vm.isPlaying = false
            }
        }

        binding.homeBtn.setOnClickListener {
            findNavController().navigate(R.id.mainFragment)
        }

        binding.streamsBtn.setOnClickListener {
            findNavController().navigate(R.id.streamsFragment)
        }

        return binding.root
    }

    fun updatePlayer(){
        (activity as MainActivity).startService(
            Intent(
                context,
                MediaPlayerService::class.java
            ).also {
                it.putExtra("STREAM", vm.currentStreamLive.value)
                it.putExtra("ACTION", "switch")
                if(vm.currentAuthorLive.value != null && vm.currentSongLive.value!= null) {
                    it.putExtra("SONG", vm.currentSongLive.value)
                    it.putExtra("ARTIST", vm.currentAuthorLive.value)
                }
                else{
                    it.putExtra("SONG", "You are listening to")
                    it.putExtra("ARTIST", "Radio Myata")
                }
            })
        if(!vm.isPlaying) {
            binding.btnPlay.setImageResource(R.drawable.btn_play)
        }
        else {
            binding.btnPlay.setImageResource(R.drawable.pause_btn)
        }

    }

}