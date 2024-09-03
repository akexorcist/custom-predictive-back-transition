package com.skooldio.backgesture

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.skooldio.backgesture.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonNextActivity.setOnClickListener {
            startActivity(Intent(this, NextActivity::class.java))
        }

        binding.buttonNextFragment.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .add(binding.fragmentContainer.id, DetailFragment(), null)
                .addToBackStack(null)
                .commit()
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(binding.fragmentContainer.id, MainFragment(), null)
                .commit()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(Activity.OVERRIDE_TRANSITION_OPEN, R.anim.anim_open_activity_enter, R.anim.anim_open_activity_exit)
            overrideActivityTransition(Activity.OVERRIDE_TRANSITION_CLOSE, R.anim.anim_close_activity_enter, R.anim.anim_close_activity_exit)
        }
    }
}
