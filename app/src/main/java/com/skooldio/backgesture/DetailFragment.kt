package com.skooldio.backgesture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.BackEventCompat
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.skooldio.backgesture.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding

    private val background: LinearLayout
        get() = binding.main

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val callback = object : OnBackPressedCallback(enabled = true) {
            var initialTouchY = -1f

            override fun handleOnBackStarted(backEvent: BackEventCompat) {
            }

            override fun handleOnBackProgressed(backEvent: BackEventCompat) {
                val progress = GestureInterpolator.getInterpolation(backEvent.progress)
                if (initialTouchY < 0f) {
                    initialTouchY = backEvent.touchY
                }
                // Shift horizontally.
                val maxTranslationX = (background.width / 1.75f)
                background.translationX = progress * maxTranslationX *
                        (if (backEvent.swipeEdge == BackEventCompat.EDGE_LEFT) 1 else -1)
            }

            override fun handleOnBackPressed() {
                parentFragmentManager.popBackStack()
            }

            override fun handleOnBackCancelled() {
                // If the user cancels the back gesture, reset the state
                initialTouchY = -1f
                background.translationX = 0f
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }
}
