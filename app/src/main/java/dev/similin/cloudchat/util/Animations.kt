package dev.similin.cloudchat.util

import android.animation.ObjectAnimator
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup

fun slideUp(view: View) {
    view.post {
        val height = view.height
        val valueAnimator = ObjectAnimator.ofInt(height, 0)
        valueAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            if (value > 0) {
                val layoutParams = view.layoutParams
                layoutParams.height = value
                view.layoutParams = layoutParams
            } else {
                view.visibility = View.GONE
            }
        }
        valueAnimator.start()
    }
}

fun slideDown(view: View) {
    view.visibility = View.VISIBLE
    val layoutParams = view.layoutParams
    layoutParams.height = 1
    view.layoutParams = layoutParams
    view.measure(
        View.MeasureSpec.makeMeasureSpec(
            Resources.getSystem().displayMetrics.widthPixels,
            View.MeasureSpec.EXACTLY
        ),
        View.MeasureSpec.makeMeasureSpec(
            0,
            View.MeasureSpec.UNSPECIFIED
        )
    )
    val height = view.measuredHeight
    val valueAnimator = ObjectAnimator.ofInt(1, height)
    valueAnimator.addUpdateListener { animation ->
        val value = animation.animatedValue as Int
        if (height > value) {
            val layoutParams1 = view.layoutParams
            layoutParams1.height = value
            view.layoutParams = layoutParams1
        } else {
            val layoutParams1 = view.layoutParams
            layoutParams1.height = ViewGroup.LayoutParams.WRAP_CONTENT
            view.layoutParams = layoutParams1
        }
    }
    valueAnimator.start()
}