package com.example.deskpad

class GestureInterpreter(private val mouseManager: VirtualMouseManager) {

    fun onSingleFingerMove(dx: Float, dy: Float) {
        mouseManager.moveCursor(dx, dy)
    }

    fun onTwoFingerScroll(dy: Float) {
        // dy is from Compose's transform gesture
        mouseManager.scroll(dy)
    }

    fun onClick() {
        mouseManager.click()
    }

    fun onDoubleClick() {
        mouseManager.doubleClick()
    }

    fun onDragStart() {
        mouseManager.startDrag()
    }

    fun onDragEnd() {
        mouseManager.endDrag()
    }
}
