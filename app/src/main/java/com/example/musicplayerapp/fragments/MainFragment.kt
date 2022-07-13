package com.example.musicplayerapp.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayerapp.MainActivity
import com.example.musicplayerapp.R
import com.example.musicplayerapp.StreamsViewModel
import com.example.musicplayerapp.adapters.PlaylistAdapter
import com.example.musicplayerapp.databinding.FragmentMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser
import org.jsoup.select.Elements
import java.io.IOException


class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding
    var playlistPicsUri = mutableListOf<Uri>()
    var playlistNames = mutableListOf<String>()
    lateinit var vm: StreamsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        vm = (activity as MainActivity).viewModel

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main, container, false
        )

//        GlobalScope.launch {
//            getPlaylistsInfo()
//        }

        binding.infoBtn.setOnClickListener {
            findNavController().navigate(R.id.infoFragment)
        }

        binding.myataStreamBanner.setOnClickListener {
            vm.currentStreamLive.value = "myata"
            findNavController().navigate(R.id.player_myata)
        }

        binding.goldStreamBanner.setOnClickListener {
            vm.currentStreamLive.value = "gold"
            findNavController().navigate(R.id.player_myata)
        }

        binding.xtraStreamBanner.setOnClickListener {
            vm.currentStreamLive.value = "myata_hits"
            findNavController().navigate(R.id.player_myata)
        }

        binding.donateBtn.setOnClickListener {
            findNavController().navigate(R.id.donate)
        }

        binding.playlists.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.playlists.adapter = PlaylistAdapter(playlistPicsUri,playlistNames)


        // Inflate the layout for this fragment
        return binding.root
    }

    //нужно понять какой запрос отправлять, затем Retrofit -> playlists.jsx
    private fun getPlaylistsInfo() {
        try {
            val doc:Document = Jsoup.parse(
                "https://music.yandex.ru/users/shaldin.voice/playlists", "", Parser.xmlParser())
            val elements: Elements = doc.select("div[class=playlist playlist_selectable]")
            for (el in elements){
                playlistPicsUri.add(Uri.parse("https:"+el.select(
                    "img[class=playlist-cover__img deco-pane]").attr("srcset")))
                playlistNames.add(el.select(
                    "div[class=playlist__title deco-typo typo-main]").attr("title"))

                Log.d("PLLST", "https:"+el.select(
                    "img[class=playlist-cover__img deco-pane]").attr("srcset") + "========" + el.select(
                    "div[class=playlist__title deco-typo typo-main]").attr("title"))
            }
        } catch (e: IOException){
            Log.e("IOException", "smth wrong with image parse")
        }
    }

}