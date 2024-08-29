package com.canerture.rivexml

import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import app.rive.runtime.kotlin.core.ExperimentalAssetLoader
import com.canerture.rivexml.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @OptIn(ExperimentalAssetLoader::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(binding) {
            riveView.setRiveResource(resId = R.raw.login_anim, stateMachineName = STATE_MACHINE)

            etEmail.setOnFocusChangeListener { _, hasFocus ->
                riveView.setBooleanState(STATE_MACHINE, "Check", hasFocus)
            }

            etPassword.setOnFocusChangeListener { _, hasFocus ->
                riveView.setBooleanState(STATE_MACHINE, "hands_up", hasFocus)
            }

            etEmail.doOnTextChanged { text, _, _, _ ->
                text?.let {
                    riveView.setNumberState(STATE_MACHINE, "Look", 2 * it.length.toFloat())
                }
            }

            btnLogin.setOnClickListener {
                etEmail.clearFocus()
                etPassword.clearFocus()
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                Handler(mainLooper).postDelayed({
                    if (email == "cnrture@gmail.com" && password == "123456") {
                        riveView.fireState(STATE_MACHINE, "success")
                    } else {
                        riveView.fireState(STATE_MACHINE, "fail")
                    }
                }, 1150L)
            }
        }
    }

    companion object {
        private const val STATE_MACHINE = "State Machine 1"
    }
}