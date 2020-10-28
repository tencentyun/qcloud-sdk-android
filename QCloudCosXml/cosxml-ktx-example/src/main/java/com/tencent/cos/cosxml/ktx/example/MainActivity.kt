package com.tencent.cos.cosxml.ktx.example

import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                if (counter.text.isEmpty()) {
                    counter.text = "0"
                } else {
                    counter.text = (counter.text.toString().toInt() + 1).toString()
                }
                sendEmptyMessageDelayed(0, 1000)
            }
        }

        handler?.sendEmptyMessageDelayed(0, 1000)

        hello.setOnClickListener {
            viewModel.downloadObject(this, download_counter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        handler?.let {
            it.removeCallbacksAndMessages(null)
        }
    }
}
