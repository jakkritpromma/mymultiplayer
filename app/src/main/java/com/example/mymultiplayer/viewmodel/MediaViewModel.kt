package com.example.mymultiplayer.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.mymultiplayer.model.Folder
import com.example.mymultiplayer.model.MediaModel
import java.io.File

class MediaViewModel() : ViewModel() {
    val TAG = MediaViewModel::class.simpleName

    @SuppressLint("Range") fun getAllMedia(context: Context): ArrayList<MediaModel> {
        val sortEditor = context.getSharedPreferences("Sorting", AppCompatActivity.MODE_PRIVATE)
        var sortValue: Int = sortEditor.getInt("sortValue", 0)
        var folderList = ArrayList<Folder>()
        val tempList = ArrayList<MediaModel>()
        val tempFolderList = ArrayList<String>()
        /*  val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.BUCKET_ID)
        val sortList = arrayOf(
            MediaStore.Audio.Media.DATE_ADDED + " DESC",
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.TITLE + " DESC",
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.SIZE + " DESC")*/ //val cursor = context.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, sortList[sortValue])
        val cursor = context.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null)
        Log.d(TAG, "cursor: $cursor")
        if (cursor != null) if (cursor.moveToNext()) do {
            val titleMD = cursor.getString(cursor.getColumnIndex(MediaStore.Downloads.TITLE))
            Log.d(TAG, "titleMD: $titleMD")
            val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
            val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
            val folderC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME))
            val folderIdC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.BUCKET_ID))
            val sizeC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE))
            val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
            val durationC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))?.toLong() ?: 0L
            try {
                Log.d(TAG, "pathC $pathC")
                Log.d(TAG, "folderC $folderC")
                val file = File(pathC)
                val artUriC = Uri.fromFile(file)
                val mediaModel = MediaModel(title = titleC, id = idC, folderName = folderC, duration = durationC, size = sizeC, path = pathC, artUri = artUriC)
                if (file.exists()) tempList.add(mediaModel)

                if (!tempFolderList.contains(folderC)) {
                    tempFolderList.add(folderC)
                    folderList.add(Folder(id = folderIdC, folderName = folderC))
                }
            } catch (e: Exception) {
                Log.e(TAG, "e: $e")
            }
        } while (cursor.moveToNext())
        cursor?.close()
        return tempList
    }
}