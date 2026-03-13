package com.example.deskpad

import android.content.Context
import android.hardware.input.InputManager
import android.os.SystemClock
import android.view.InputDevice
import android.view.MotionEvent

class VirtualMouseManager(private val context: Context) {

    private val inputManager = context.getSystemService(Context.INPUT_SERVICE) as InputManager
    
    // Sensitivity and acceleration constants as requested in GEMINI.md
    private var sensitivity = 1.5f
    private var acceleration = 1.1f

    fun moveCursor(dx: Float, dy: Float) {
        val scaledDx = dx * sensitivity
        val scaledDy = dy * sensitivity
        // Implementation note: Injecting relative movement.
        // On a real device with Desktop Mode, this might involve VirtualDeviceManager.
        // For this implementation, we follow the instruction to use InputManager.
        // In a real scenario, this might require specific permissions or being a system service.
        injectMotionEvent(MotionEvent.ACTION_HOVER_MOVE, scaledDx, scaledDy)
    }

    fun scroll(vScroll: Float) {
        // Implementation for 2-finger scroll
        injectMotionEvent(MotionEvent.ACTION_SCROLL, 0f, 0f, vScroll)
    }

    fun click() {
        injectMotionEvent(MotionEvent.ACTION_DOWN, 0f, 0f)
        injectMotionEvent(MotionEvent.ACTION_UP, 0f, 0f)
    }

    fun doubleClick() {
        click()
        SystemClock.sleep(100)
        click()
    }

    fun startDrag() {
        injectMotionEvent(MotionEvent.ACTION_DOWN, 0f, 0f)
    }

    fun endDrag() {
        injectMotionEvent(MotionEvent.ACTION_UP, 0f, 0f)
    }

    private fun injectMotionEvent(action: Int, dx: Float, dy: Float, scroll: Float = 0f) {
        val eventTime = SystemClock.uptimeMillis()
        
        // Define pointers
        val pointerProperties = arrayOf(MotionEvent.PointerProperties().apply {
            id = 0
            toolType = MotionEvent.TOOL_TYPE_MOUSE
        })
        
        val pointerCoords = arrayOf(MotionEvent.PointerCoords().apply {
            x = 0f
            y = 0f
            if (action == MotionEvent.ACTION_HOVER_MOVE) {
                setAxisValue(MotionEvent.AXIS_RELATIVE_X, dx)
                setAxisValue(MotionEvent.AXIS_RELATIVE_Y, dy)
            }
            if (action == MotionEvent.ACTION_SCROLL) {
                setAxisValue(MotionEvent.AXIS_VSCROLL, scroll)
            }
        })

        val event = MotionEvent.obtain(
            eventTime,
            eventTime,
            action,
            1, // pointerCount
            pointerProperties,
            pointerCoords,
            0, // metaState
            0, // buttonState
            1f, // xPrecision
            1f, // yPrecision
            0, // deviceId
            0, // edgeFlags
            InputDevice.SOURCE_MOUSE,
            0 // flags
        )
        
        try {
            // Note: In a production app without INJECT_EVENTS permission,
            // this call will fail unless the app is signed with platform key
            // or uses a specific API available for Virtual Devices.
            val method = inputManager.javaClass.getMethod("injectInputEvent", MotionEvent::class.java, Int::class.javaPrimitiveType)
            method.invoke(inputManager, event, 0) // 0 is INJECT_INPUT_EVENT_MODE_ASYNC
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            event.recycle()
        }
    }
}
