package com.fsoftstudio.kinopoisklite.presentation

import android.view.View


fun View.animateDefault(startDelay: Long = 0) {
    this.animate().alpha(0.0F).alpha(1.0F).setStartDelay(startDelay).duration = 500
}

fun View.animateWithStartDelay(startDelay: Long) {
    this.animateDefault(startDelay)
}
