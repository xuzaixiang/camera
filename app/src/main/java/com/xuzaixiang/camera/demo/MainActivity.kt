package com.xuzaixiang.camera.demo

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        Handler(mainLooper).postDelayed({
//            registerForActivityResult(ActivityResultContracts.RequestPermission()){
//
//            }.launch(Manifest.permission.CAMERA)
//        },2000)
    }
}