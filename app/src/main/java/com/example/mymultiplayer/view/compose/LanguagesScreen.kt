package com.example.mymultiplayer.view.compose

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.mymultiplayer.viewmodel.LanguagesViewModel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.mymultiplayer.R

@Composable fun LanguagesScreen(navController: NavController, languagesViewModel: LanguagesViewModel = viewModel()) {
    val tag = "LanguagesScreen"

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(50.dp)) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(languagesViewModel.itemList.size) { i ->
                Row(modifier = Modifier.height(80.dp)) {
                    val data = languagesViewModel.itemList.get(i)
                    val mutableSetFlags: MutableSet<String>? = data.flags?.keySet()
                    if (mutableSetFlags != null) {
                        Log.d(tag, "mutableSetFlags.toList().get(0): " + mutableSetFlags.toList().get(0))
                        val firstKey = mutableSetFlags.toList().get(0)
                        val firstValue = data.flags.get(firstKey).toString()
                        val firstImgLink = firstValue.substring(1, firstValue.length - 1)
                        Log.d(tag, "firstImgLink: $firstImgLink")

                        Image(
                            modifier = Modifier.weight(2f),
                            painter = rememberAsyncImagePainter(firstImgLink),
                            contentDescription = "Image from URL",
                        )
                    }
                    Column(modifier = Modifier.weight(8f)) {
                        val commonName = data.name?.get("common").toString()
                        Text(modifier = Modifier.weight(1f), fontSize = 20.sp, color = Color.Black, text = commonName.substring(1, commonName.length - 1), style = TextStyle(fontWeight = FontWeight.Bold))
                        val mutableSetLang: MutableSet<String>? = data.languages?.keySet()
                        if (mutableSetLang != null) {
                            Log.d(tag, "mutableSet.toList().get(0): " + mutableSetLang.toList().get(0))
                            val firstKey = mutableSetLang.toList().get(0)
                            val firstValue = data.languages.get(firstKey).toString()
                            val language = firstValue.substring(1, firstValue.length - 1)
                            Log.d(tag, "language: $language")
                            Text(modifier = Modifier.weight(1f), fontSize = 20.sp, color = Color.Black, text = language)
                        }
                    }
                }
            }
        }
        Button(onClick = { navController.navigate(R.id.action_langaugesFragment_to_settingFragment) }, modifier = Modifier.align(Alignment.BottomEnd), colors = ButtonDefaults.buttonColors(containerColor = Color.Black, // Set the button background to black
            contentColor = Color.White
        )) {
            Text(color = Color.White, text = "Back")
        }
    }

}