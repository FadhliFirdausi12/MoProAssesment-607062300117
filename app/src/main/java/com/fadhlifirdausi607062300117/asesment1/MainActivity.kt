package com.fadhlifirdausi607062300117.asesment1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.fadhlifirdausi607062300117.asesment1.navigation.SetUpNavGraph
import com.fadhlifirdausi607062300117.asesment1.ui.theme.AppTheme
import com.fadhlifirdausi607062300117.asesment1.util.SettingsDataStore

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            Asesment1Theme {
//               SetUpNavGraph()
//            }
//        }
//    }
//}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val dataStore = SettingsDataStore(this)
        setContent {
            val isDarkTheme by dataStore.themeFlow.collectAsState(initial = false)
            AppTheme(isDarkTheme = isDarkTheme) {
                SetUpNavGraph()
            }
        }
    }
}


