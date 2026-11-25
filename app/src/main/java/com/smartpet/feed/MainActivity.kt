package com.smartpet.feed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.smartpet.feed.ui.AppRoot
import com.smartpet.feed.ui.theme.SmartPetFeedTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartPetFeedTheme {
                AppRoot()
            }
        }
    }
}