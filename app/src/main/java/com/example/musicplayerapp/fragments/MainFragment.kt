package com.example.musicplayerapp.fragments

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayerapp.R
import com.example.musicplayerapp.adapters.PlaylistAdapter
import com.example.musicplayerapp.adapters.StreamAdapter
import com.example.musicplayerapp.databinding.FragmentMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException


class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding
    var playlistPicsUri = mutableListOf<Uri>()
    var playlistNames = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main, container, false
        )

//        GlobalScope.launch {
//            getPlaylistsInfo()
//        }

        binding.streams.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.streams.adapter = StreamAdapter(listOf(R.drawable.ic_splash_1,R.drawable.ic_splash_1,R.drawable.ic_splash_1))

        binding.playlists.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.playlists.adapter = PlaylistAdapter(playlistPicsUri,playlistNames)

        binding.toStream.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_streamsFragment2)
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun getPlaylistsInfo() {
        try {
            val doc:Document = Jsoup.connect(
                "https://music.yandex.ru/users/shaldin.voice/playlists"
            ).userAgent("Opera").get()
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