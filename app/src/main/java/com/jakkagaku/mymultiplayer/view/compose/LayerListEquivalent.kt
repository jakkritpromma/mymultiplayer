package com.jakkagaku.mymultiplayer.view.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White

@Composable fun LayerListEquivalent() {
    Box(modifier = Modifier.size(100.dp) // Size of the outer container
    ) { // First Layer: Gradient with padding
        Box(modifier = Modifier
            .background(Brush.verticalGradient(colors = listOf(White, Black)))
            .padding(5.dp) // Padding around the first layer
            .align(Alignment.Center) // Aligns to the center
            .clip(RoundedCornerShape(10.dp)) // Rounded corners
        ) {}

        // Second Layer: Solid color with size and rounded corners
        Box(modifier = Modifier
            .size(50.dp) // Size of the second layer
            .background(Color.Red) // Solid color
            .align(Alignment.Center) // Aligns to the center
            .clip(RoundedCornerShape(10.dp)) // Rounded corners
        ) {}
    }
}

@Preview(showBackground = true) @Composable fun PreviewLayerListEquivalent() {
    LayerListEquivalent()
}
