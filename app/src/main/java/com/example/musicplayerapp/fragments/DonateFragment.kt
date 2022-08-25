package com.example.musicplayerapp.fragments

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.musicplayerapp.MainActivity
import com.example.musicplayerapp.R
import com.example.musicplayerapp.StreamsViewModel
import com.example.musicplayerapp.databinding.FragmentDonateBinding


class DonateFragment : Fragment() {

    lateinit var vm: StreamsViewModel
    lateinit var binding: FragmentDonateBinding
    var ifAc: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_donate, container, false
        )

        vm = (activity as MainActivity).viewModel

        binding.switchAc.setOnCheckedChangeListener { compoundButton, b ->
            ifAc = !b
        }

        vm.isInSplitMode.observe(viewLifecycleOwner, Observer {
            if(it){
                binding.commentText.visibility = View.GONE
            }
            else{
                binding.commentText.visibility = View.VISIBLE
            }
        })

        binding.sendBtn.setOnClickListener{

            if(binding.summ.text!=null){
                if(!binding.summ.text.isBlank()){
                    var summ = binding.summ.text.toString().toInt()
                    if(summ <= 15000 && summ >= 100){
                        binding.form.visibility = View.GONE
                        binding.webView.visibility = View.VISIBLE
                        setWebViewClient()
                        makePay(summ, binding.commentText.text.toString(), if(ifAc) "ac" else "bc")
                    }
                }
            }
        }

        (activity as MainActivity).binding.playerBtn.setOnClickListener {
            findNavController().navigate(R.id.player, Bundle().apply {
                when(vm.currentStreamLive.value){
                    "myata"->putInt(CURRENT_ITEM, 0)
                    "gold"->putInt(CURRENT_ITEM, 1)
                    "myata_hits"->putInt(CURRENT_ITEM, 2)
                }
            })
        }

        (activity as MainActivity).binding.infoBtn.setOnClickListener {
            findNavController().navigate(R.id.info)
        }
        (activity as MainActivity).binding.homeBtn.setOnClickListener {
            findNavController().navigate(R.id.home)
        }

        binding.summ.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0!=null){
                    if(!p0.isBlank()){
                        if(p0.toString().toInt() <= 15000 && p0.toString().toInt() >= 100)
                            binding.summ.background.setColorFilter(Color.parseColor("#5FD9B4"), PorterDuff.Mode.MULTIPLY)
                        else
                            binding.summ.background.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY)
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        return binding.root
    }

    private fun makePay(sum:Int, comment: String, paymentType: String)
    {
        val order_id = "123"
        val url = "https://yoomoney.ru/quickpay/confirm.xml"
        val postData = "receiver=410015757768507" +
                "&formcomment=" + comment +
                "&short-dest=" + "Пожертовование Radio Myata"+
                "&label=$order_id" +
                "&quickpay-form=donate" +
                "&targets=" + "Пожертовование Radio Myata" +
                "&sum=$sum" +
                "&comment=" + comment
                "&need-fio=false" +
                "&need-email=false" +
                "&need-phone=false" +
                "&need-address=false" +
                "&paymentType=${paymentType}"

        binding.webView.postUrl(url, postData.toByteArray())
    }

    private fun setWebViewClient()
    {
        val web_view = binding.webView
        val settings = web_view.settings
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true

        binding.webView.webViewClient = object : WebViewClient()
        {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean
            {
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?)
            {
                Log.e("ActPayment", "WebPage finished $url")

                super.onPageFinished(view, url)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.summ.background.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)
    }
}