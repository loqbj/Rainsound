package com.example.rainsound

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.appcompat.content.res.AppCompatResources

class DraggableCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    init {
        isClickable = true
        isFocusable = true

        // Use AppCompatResources for backward compatibility
        foreground = AppCompatResources.getDrawable(
            context,
            android.R.drawable.list_selector_background
        )

        setOnClickListener {
            Toast.makeText(context, "Card clicked!", Toast.LENGTH_SHORT).show()
        }

        setOnTouchListener { view, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    view.performClick()
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    view.x = event.rawX - view.width / 2
                    view.y = event.rawY - view.height / 2
                    true
                }
                else -> false
            }
        }
    }
}
