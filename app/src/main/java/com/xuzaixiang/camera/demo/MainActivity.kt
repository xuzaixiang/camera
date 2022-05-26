package com.xuzaixiang.camera.demo

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    private val permission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            val granted = it.all { it.value }
            if (granted){
                setContentView(R.layout.activity_main)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permission.launch(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO))
    }
}