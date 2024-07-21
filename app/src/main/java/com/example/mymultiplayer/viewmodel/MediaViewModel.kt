package com.example.mymultiplayer.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.mymultiplayer.model.Folder
import com.example.mymultiplayer.model.Video
import java.io.File

class MediaViewModel() : ViewModel() {
    val TAG = MediaViewModel::class.simpleName

    @SuppressLint("InlinedApi", "Recycle", "Range") fun getAllVideos(context: Context): ArrayList<Video> {
        val sortEditor = context.getSharedPreferences("Sorting", AppCompatActivity.MODE_PRIVATE)
        var sortValue: Int = sortEditor.getInt("sortValue", 0)
        var folderList = ArrayList<Folder>()
        val tempList = ArrayList<Video>()
        val tempFolderList = ArrayList<String>()
        val projection = arrayOf(MediaStore.Video.Media.TITLE, MediaStore.Video.Media.SIZE, MediaStore.Video.Media._ID, MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATA, MediaStore.Video.Media.DATE_ADDED, MediaStore.Video.Media.DURATION, MediaStore.Video.Media.BUCKET_ID)
        val sortList = arrayOf(MediaStore.Video.Media.DATE_ADDED + " DESC", MediaStore.Video.Media.DATE_ADDED, MediaStore.Video.Media.TITLE, MediaStore.Video.Media.TITLE + " DESC", MediaStore.Video.Media.SIZE, MediaStore.Video.Media.SIZE + " DESC")
        val cursor = context.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, sortList[sortValue])
        if (cursor != null) if (cursor.moveToNext()) do {
            val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE)) ?: "Unknown"
            val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID)) ?: "Unknown"
            val folderC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)) ?: "Internal Storage"
            val folderIdC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID)) ?: "Unknown"
            val sizeC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE)) ?: "0"
            val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)) ?: "Unknown"
            val durationC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION))?.toLong() ?: 0L
            try {
                Log.d(TAG, "pathC $pathC")
                if (folderC.contains("Camera")) {
                    Log.d(TAG, "folderC $folderC")
                    val file = File(pathC)
                    val artUriC = Uri.fromFile(file)
                    val video = Video(title = titleC, id = idC, folderName = folderC, duration = durationC, size = sizeC, path = pathC, artUri = artUriC)
                    if (file.exists()) tempList.add(video)

                    //for adding folders
                    if (!tempFolderList.contains(folderC) && !folderC.contains("Internal Storage")) {
                        tempFolderList.add(folderC)
                        folderList.add(Folder(id = folderIdC, folderName = folderC))
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "e: $e")
            }
        } while (cursor.moveToNext())
        cursor?.close()
        return tempList
    }
}