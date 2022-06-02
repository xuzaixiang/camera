package com.xuzaixiang.camera.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class EntryActivity : AppCompatActivity() {

    private val tvMain:TextView by lazy { findViewById(R.id.tv_main) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)
        tvMain.setOnClickListener{
            MainActivity.launch(this)
        }
    }
}