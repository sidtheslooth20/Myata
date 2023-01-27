package com.example.musicplayerapp.fragments

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import com.example.musicplayerapp.MainActivity
import com.example.musicplayerapp.R
import com.example.musicplayerapp.StreamsViewModel
import com.example.musicplayerapp.databinding.FragmentMyataStreamBinding
import com.example.musicplayerapp.service.MediaPlayerService
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation


const val STREAM = "myata"

class MyataStreamFragment() : Fragment() {


    lateinit var vm: StreamsViewModel
    lateinit var binding: FragmentMyataStreamBinding
    var stream: String = "myata"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        vm = (activity as MainActivity).viewModel

        vm.currentFragmentLiveData.value = "player"
        vm.ifNeedToNavigateStraightToPlayer = false

        arguments?.takeIf { it.containsKey(STREAM) }?.apply {
            stream = getString(STREAM).toString()
        }

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_myata_stream, container, false
        )

        binding.mainAuthor.text = ""

        vm.isPlaying.observe(viewLifecycleOwner, Observer {
            if(it){
                binding.btnPlay.setImageResource(R.drawable.pause_btn)
            }
            else{
                binding.btnPlay.setImageResource(R.drawable.btn_play)
            }
        })

        when(stream){
            "myata"->{
                binding.constraintLayout.setBackgroundResource(R.drawable.myata_bg)
                vm.currentMyataState.observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        updateUI(it)
                    }
                })
            }
            "gold"-> {
                binding.constraintLayout.setBackgroundResource(R.drawable.gold_bg)
                vm.currentGoldState.observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        updateUI(it)
                    }
                })
            }
            "myata_hits"->{
                binding.constraintLayout.setBackgroundResource(R.drawable.xtra_bg)
                vm.currentXtraState.observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        updateUI(it)
                    }
                })
            }
        }

        binding.btnPlay.setOnClickListener {
            vm.ifNeedToListenReciever = true
            (activity as MainActivity).startService(
                Intent(
                    context,
                    MediaPlayerService::class.java
                ).also {
                    it.putExtra("STREAM", vm.currentStreamLive.value)
                    it.putExtra("ACTION", "startStop")
                })
        }

        vm.isInSplitMode.observe(viewLifecycleOwner, Observer {
            if (vm.isInSplitMode.value!!){
                binding.photo.visibility = View.GONE
                binding.logo.visibility = View.GONE
                (activity as MainActivity).binding.bottomNavView.visibility = View.GONE
            }
        })

        vm.currentStreamLive.observe(viewLifecycleOwner, Observer {

            var intent = Intent()
            intent.setAction("switch_track")
            when(it){
                "myata"->{
                    intent.putExtra("artist",vm.currentMyataState.value?.artist)
                    intent.putExtra("song",vm.currentMyataState.value?.song)
                    (activity as MainActivity).binding.playerBtn.setColorFilter(Color.parseColor("#FFCCFF"))
                }
                "gold"->{
                    intent.putExtra("artist",vm.currentGoldState.value?.artist)
                    intent.putExtra("song",vm.currentGoldState.value?.song)
                    (activity as MainActivity).binding.playerBtn.setColorFilter(Color.parseColor("#FF3F7B"))
                }
                "myata_hits"->{
                    intent.putExtra("artist",vm.currentXtraState.value?.artist)
                    intent.putExtra("song",vm.currentXtraState.value?.song)
                    (activity as MainActivity).binding.playerBtn.setColorFilter(Color.parseColor("#FF3F7B"))
                }
            }
            context?.let { it1 ->
                LocalBroadcastManager.getInstance(it1)
                    .sendBroadcast(intent).apply {}
            }
        })

        (activity as MainActivity).binding.homeBtn.setOnClickListener {
            findNavController().navigate(R.id.home)
        }
        (activity as MainActivity).binding.infoBtn.setOnClickListener {
            findNavController().navigate(R.id.info)
        }
        (activity as MainActivity).binding.donateBtn.setOnClickListener {
            findNavController().navigate(R.id.donate)
        }

        return binding.root
    }

    override fun onResume() {
        vm.currentFragmentLiveData.value = "player"

        updatePlayer()

        when(stream){
            "myata"->{
                vm.currentMyataState.value?.let { updateUI(it) }
            }
            "gold"-> {
                vm.currentGoldState.value?.let { updateUI(it) }
            }
            "myata_hits"->{
                vm.currentXtraState.value?.let { updateUI(it) }
            }
        }

        if (!vm.isInSplitMode.value!!){
            binding.photo.visibility = View.VISIBLE
            binding.logo.visibility = View.VISIBLE
            (activity as MainActivity).binding.bottomNavView.visibility = View.VISIBLE
        }

        Log.d("PLAYER", "resume")
        super.onResume()
    }

    fun updatePlayer(){
        (activity as MainActivity).startService(
            Intent(
                context,
                MediaPlayerService::class.java
            ).also {
                it.putExtra("STREAM", vm.currentStreamLive.value)
                it.putExtra("ACTION", "switch")
                vm.ifNeedToListenReciever = false
                when(vm.currentStreamLive.value){
                    "myata"->{
                        if(vm.currentMyataState.value!!.song != null && vm.currentMyataState.value!!.artist != null) {
                            it.putExtra("SONG", vm.currentMyataState.value!!.song)
                            it.putExtra("ARTIST", vm.currentMyataState.value!!.artist)
                        }
                        else{
                            it.putExtra("SONG", "You are listening to")
                            it.putExtra("ARTIST", "Radio Myata")
                        }
                    }
                    "gold"->{
                        if(vm.currentGoldState.value!!.song != null && vm.currentGoldState.value!!.artist != null) {
                            it.putExtra("SONG", vm.currentGoldState.value!!.song)
                            it.putExtra("ARTIST", vm.currentGoldState.value!!.artist)
                        }
                        else{
                            it.putExtra("SONG", "You are listening to")
                            it.putExtra("ARTIST", "Radio Myata")
                        }
                    }
                    "myata_hits"->{
                        if(vm.currentXtraState.value!!.song != null && vm.currentXtraState.value!!.artist != null) {
                            it.putExtra("SONG", vm.currentXtraState.value!!.song)
                            it.putExtra("ARTIST", vm.currentXtraState.value!!.artist)
                        }
                        else{
                            it.putExtra("SONG", "You are listening to")
                            it.putExtra("ARTIST", "Radio Myata")
                        }
                    }
                }
            })
    }

    fun updateUI(it: StreamsViewModel.PlayerState){
        binding.logo.setImageResource(R.drawable.logo)
        if (it != null) {
            if(it.artist!=null) {
                if(!it.artist!!.isBlank()) {
                    if (it.img != null) {
                        if(!it.img!!.isBlank()){
                            Picasso.get()
                                .load(Uri.parse(it.img!!.replace("avatar170", "avatar770")))
                                .transform(CropCircleTransformation())
                                .fit()
                                .centerCrop().into(binding.photo)
                        }
                        else{
                            binding.photo.setImageResource(R.drawable.logo)
                        }
                    } else {
                        binding.photo.setImageResource(R.drawable.logo)
                    }

                    binding.mainSong.text = it.song
                    binding.mainAuthor.text = it.artist
                }
                else{
                    binding.mainAuthor.text = "YOU ARE LISTENING"
                    binding.mainSong.text = "RADIO MYATA"
                    binding.photo.setImageResource(R.drawable.logo)
                }
            }
        }
        else {
            binding.mainAuthor.text = "YOU ARE LISTENING"
            binding.mainSong.text = "RADIO MYATA"
            binding.photo.setImageResource(R.drawable.logo)
        }
    }
}