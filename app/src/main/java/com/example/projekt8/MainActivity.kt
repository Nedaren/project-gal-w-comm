package com.example.projekt8

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


private var imageRecycler: RecyclerView? = null
@SuppressLint("StaticFieldLeak")
private var progressBar: ProgressBar? = null
private var allPictures: ArrayList<Image>? = null

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageRecycler = findViewById(R.id.image_recycler)
        progressBar = findViewById(R.id.recycler_progress)
        imageRecycler?.setHasFixedSize(true)

        //Storage Permissions
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 101
            )
        }

        allPictures = ArrayList()

        if (allPictures!!.isEmpty()) {
            progressBar?.visibility = View.VISIBLE
            //get All Images From Storage
            allPictures = getAllImages()
            //Set Adapter to recycler
            imageRecycler?.adapter = ImageAdapter(this, allPictures!!)
            progressBar?.visibility = View.GONE
        }
    }

    private fun getAllImages(): ArrayList<Image> {
        val images = ArrayList<Image>()
        val allImageUri = Media.EXTERNAL_CONTENT_URI
        val projection =
            arrayOf(MediaStore.Images.ImageColumns.DATA, Media.DISPLAY_NAME)
        var cursor =
            this@MainActivity.contentResolver.query(allImageUri, projection, null, null, null)
        val database = AppDatabase.getInstance(applicationContext)
        val imageDAO = database.imageDao()

        lifecycleScope.launch{
            withContext(Dispatchers.IO) {
                try {
                    cursor!!.moveToFirst()
                    do {
                        val image = Image(
                            imagePath = cursor.getString(cursor.getColumnIndexOrThrow(Media.DATA)),
                            imageName = cursor.getString(cursor.getColumnIndexOrThrow(Media.DISPLAY_NAME)),
                            imageComment = " "
                        )
                        images.add(image)
                        if(!checkIfDataExists(image.imagePath)) {
                            imageDAO.insert(image)
                        }
                    } while (cursor.moveToNext())
                    cursor.close()
                    AppDatabase.getInstance(applicationContext).imageDao().getAll()

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return images
    }
        private fun checkIfDataExists(imagePath: String): Boolean {
            val database = AppDatabase.getInstance(applicationContext)
            val imageDao = database.imageDao()
            val cursor: List<Image> = imageDao.checkFor(imagePath)

            return cursor.isNotEmpty()
        }
}
