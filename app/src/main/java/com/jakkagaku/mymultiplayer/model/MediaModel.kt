package com.jakkagaku.mymultiplayer.model

import android.net.Uri

val TAG = MediaModel::class.simpleName

data class MediaModel(val id: String,
                      var title: String,
                      val duration: Long = 0,
                      val folderName: String,
                      val size: String,
                      var path: String,
                      var artUri: Uri)

data class Folder(val id: String, val folderName: String)

