package com.example.mymultiplayer.view.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable fun GradientSelector(
    text: String,
    textSize: Int,
    paddingValue: Int,
    textColor: Color,
    normalColor1: Color,
    normalColor2: Color,
    pressedColor1: Color,
    pressedColor2: Color,
    onClick: () -> Unit,
    isClickable: Boolean,
) {
    var isPressed by remember { mutableStateOf(false) }
    val backgroundBrush = when {
        isPressed -> Brush.linearGradient(colors = listOf(pressedColor1, pressedColor2))
        else -> Brush.linearGradient(colors = listOf(normalColor1, normalColor2))
    }

    Box(modifier = Modifier
        .padding(paddingValue.dp)
        .background(brush = backgroundBrush, shape = RoundedCornerShape(5.dp))
        .pointerInput(Unit) {
            detectTapGestures(onPress = {
                if (isClickable) {
                    isPressed = true
                    tryAwaitRelease()
                    onClick()
                    isPressed = false
                }
            })
        }) {
        Box(modifier = Modifier.padding(20.dp)) {
            Text(text = text, fontSize = textSize.sp, color = textColor, textAlign = TextAlign.Center, style = TextStyle(fontWeight = FontWeight.Bold))
        }
    }
}