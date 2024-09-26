package com.github.xingray.uiautomatorproxy.page

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.github.xingray.uiautomatorproxy.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {

    private lateinit var mViewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(mViewBinding.root)
    }
}