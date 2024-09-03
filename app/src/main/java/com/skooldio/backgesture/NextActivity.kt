package com.skooldio.backgesture

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.activity.BackEventCompat
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.skooldio.backgesture.databinding.ActivityNextBinding

class NextActivity : AppCompatActivity() {

    private val binding: ActivityNextBinding by lazy {
        ActivityNextBinding.inflate(layoutInflater)
    }

    private val background: LinearLayout
        get() = binding.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val callback = object : OnBackPressedCallback(enabled = true) {
            var initialTouchY = -1f
            val predictiveBackMargin = 0

            override fun handleOnBackStarted(backEvent: BackEventCompat) {
                Log.e("Check", "handleOnBackStarted")
            }

            override fun handleOnBackProgressed(backEvent: BackEventCompat) {
                Log.e("Check", "handleOnBackProgressed")
                val progress = GestureInterpolator.getInterpolation(backEvent.progress)
                if (initialTouchY < 0f) {
                    initialTouchY = backEvent.touchY
                }
                val progressY = GestureInterpolator.getInterpolation(
                    (backEvent.touchY - initialTouchY) / background.height
                )

                // See the motion spec about the calculations below.
                // https://developer.android.com/design/ui/mobile/guides/patterns/predictive-back#motion-specs

                // Shift horizontally.
                val maxTranslationX = (background.width / 20) - predictiveBackMargin
                background.translationX = progress * maxTranslationX *
                        (if (backEvent.swipeEdge == BackEventCompat.EDGE_LEFT) 1 else -1)

                // Shift vertically.
                val maxTranslationY = (background.height / 20) - predictiveBackMargin
                background.translationY = progressY * maxTranslationY

                // Scale down from 100% to 90%.
                val scale = 1f - (0.1f * progress)
                background.scaleX = scale
                background.scaleY = scale
            }

            override fun handleOnBackPressed() {
                Log.e("Check", "handleOnBackPressed")
                finish()
            }

            override fun handleOnBackCancelled() {
                // If the user cancels the back gesture, reset the state
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(Activity.OVERRIDE_TRANSITION_OPEN, R.anim.anim_open_activity_enter, R.anim.anim_open_activity_exit)
            overrideActivityTransition(Activity.OVERRIDE_TRANSITION_CLOSE, R.anim.anim_close_activity_enter, R.anim.anim_close_activity_exit)
//            overrideActivityTransition(Activity.OVERRIDE_TRANSITION_CLOSE, R.anim.anim_close_activity_enter, android.R.anim.slide_in_left)
        }
//        onBackPressedDispatcher.addCallback(callback)
    }
}