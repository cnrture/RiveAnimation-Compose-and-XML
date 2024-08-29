package com.canerture.rivecompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import app.rive.runtime.kotlin.core.ExperimentalAssetLoader
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val STATE_MACHINE_NAME = "State Machine 1"
private const val DELAY = 1150L

@OptIn(ExperimentalAssetLoader::class)
@Composable
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isEmailFocus by remember { mutableStateOf(false) }
    var isPasswordFocus by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    var riveView: RiveAnimationView? by remember { mutableStateOf(null) }

    LaunchedEffect(isPasswordFocus) {
        riveView?.setBooleanState(STATE_MACHINE_NAME, "hands_up", isPasswordFocus)
    }

    LaunchedEffect(isEmailFocus) {
        riveView?.setBooleanState(STATE_MACHINE_NAME, "Check", isEmailFocus)
    }

    LaunchedEffect(email) {
        riveView?.setNumberState(STATE_MACHINE_NAME, "Look", 2 * email.length.toFloat())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.1f))
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .scale(1.2f, 1.2f)
                .weight(0.5f),
            factory = { context ->
                RiveAnimationView(context).apply {
                    setRiveResource(
                        resId = R.raw.login_anim,
                        stateMachineName = STATE_MACHINE_NAME,
                        alignment = app.rive.runtime.kotlin.core.Alignment.BOTTOM_CENTER
                    )
                    riveView = this
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        isEmailFocus = focusState.isFocused
                    },
                maxLines = 1,
                placeholder = { Text("Email") },
                value = email,
                onValueChange = { email = it },
            )

            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        isPasswordFocus = focusState.isFocused
                    },
                maxLines = 1,
                placeholder = { Text("Password") },
                value = password,
                onValueChange = { password = it },
            )

            Spacer(modifier = Modifier.height(64.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                onClick = {
                    scope.launch {
                        isEmailFocus = false
                        isPasswordFocus = false
                        delay(DELAY)
                        if (email == "cnrture@gmail.com" && password == "123456") {
                            riveView?.fireState(STATE_MACHINE_NAME, "success")
                        } else {
                            riveView?.fireState(STATE_MACHINE_NAME, "fail")
                        }
                    }
                }
            ) {
                Text("Login")
            }
        }
    }
}