package com.example.musicplayerapp.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.example.musicplayerapp.R
import com.example.musicplayerapp.databinding.FragmentDonateBinding
import com.example.musicplayerapp.databinding.FragmentStreamsBinding

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

        return binding.root
    }
}