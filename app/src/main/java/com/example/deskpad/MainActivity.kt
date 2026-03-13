package com.example.deskpad

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    private lateinit var mouseManager: VirtualMouseManager
    private lateinit var gestureInterpreter: GestureInterpreter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initial mouse and gesture setup
        mouseManager = VirtualMouseManager(this)
        gestureInterpreter = GestureInterpreter(mouseManager)

        // Force Screen Stay ON and Dimmed (requested behavior in GEMINI.md)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val params = window.attributes
        params.screenBrightness = 0.01f
        window.attributes = params

        startForegroundService()

        setContent {
            DeskPadScreen(gestureInterpreter)
        }
    }

    private fun startForegroundService() {
        val serviceIntent = Intent(this, DeskPadForegroundService::class.java)
        startForegroundService(serviceIntent)
    }

    @Composable
    fun DeskPadScreen(gestureInterpreter: GestureInterpreter) {
        var rotation by remember { mutableFloatStateOf(0f) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .graphicsLayer(rotationZ = rotation)
        ) {
            // Touchpad area (all screen area)
            TouchpadComposable(
                gestureInterpreter = gestureInterpreter,
                modifier = Modifier.fillMaxSize()
            )

            // Rotation toggle button (requested behavior)
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                onClick = { 
                    rotation = if (rotation == 0f) 180f else 0f
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Rotate 180 degrees",
                    tint = Color.Gray
                )
            }
        }
    }
}
