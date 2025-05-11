package com.jakkagaku.mymultiplayer.view.compose

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jakkagaku.mymultiplayer.R
import com.jakkagaku.mymultiplayer.model.LanguageModel
import com.jakkagaku.mymultiplayer.retrofit.LanguageApiResult
import com.jakkagaku.mymultiplayer.viewmodel.LanguagesViewModel

@Composable fun LanguageListScreen(navController: NavController, languagesViewModel: LanguagesViewModel = viewModel()) {
    val tag = "LanguagesScreen"
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(brush = Brush.verticalGradient(colors = listOf(Color.White, Color(0xFF1F1F1F))))) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Gray)) {
            val state by languagesViewModel.languageState.collectAsStateWithLifecycle()
            when (state) {
                is LanguageApiResult.Loading -> {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.Gray)
                    }
                }

                is LanguageApiResult.Success -> {
                    val languages = (state as LanguageApiResult.Success<List<LanguageModel>>).data
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(20.dp)
                    ) {
                        items(languages.size) { index ->
                            LanguageItem(languages[index])
                        }
                    }
                }

                is LanguageApiResult.Error -> {
                    Text(text = "Error: ${(state as LanguageApiResult.Error).message}", color = Color.White, modifier = Modifier.padding(16.dp))
                }
            }
            Box(modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(brush = Brush.verticalGradient(colors = listOf(Color.White, Color(0xFF1F1F1F))))) {
                GradientSelector(
                    "Back", 30, 5,
                    Color.White, Color.Black, Color.Black, Color.Gray, Color.Gray,
                    onClick = {
                        navController.navigate(R.id.action_langaugesFragment_to_settingFragment)
                    },
                    isClickable = true,
                )
            }
        }
    }
}

@Composable fun LanguageItem(language: LanguageModel) {
    val name = language.name?.get("common")?.asString ?: "Unknown"
    val flagUrl = language.flags?.get("png")?.asString
    val firstKey = language.languages?.keySet()?.toList()?.get(0)
    val lang = language.languages?.get(firstKey)?.asString
    LaunchedEffect(flagUrl) {
        Log.d("LanguageItem", "flagUrl: $flagUrl")
    }
    Row(modifier = Modifier.height(80.dp)) {
        Image(
            modifier = Modifier.weight(2f),
            painter = rememberAsyncImagePainter(flagUrl),
            contentDescription = "Image from URL",
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(8f)) {
            Text(modifier = Modifier.weight(1f), text = lang.toString(), fontSize = 20.sp, color = Color.White, style = TextStyle(fontWeight = FontWeight.Bold))
            Text(modifier = Modifier.weight(1f), text = name, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}