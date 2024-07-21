package com.example.mymultiplayer.model

import android.net.Uri

val TAG = Video::class.simpleName

data class Video(val id: String,
                 var title: String,
                 val duration: Long = 0,
                 val folderName: String,
                 val size: String,
                 var path: String,
                 var artUri: Uri)

data class Folder(val id: String, val folderName: String)

