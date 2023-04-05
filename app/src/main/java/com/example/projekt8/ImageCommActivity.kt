package com.example.projekt8

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.launch

class ImageCommActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var imageDao: ImageDao

    @SuppressLint("SdCardPath")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_comm)

        val button = findViewById<Button>(R.id.open_comment)
        val imagePath = intent.getStringExtra("path")
        val imageName = intent.getStringExtra("name").toString()
        val commentSpace = findViewById<TextView>(R.id.comment_view)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app_database"
        ).createFromAsset("/data/data/com.example.project8/databases").build()

        imageDao = database.imageDao()

        lifecycleScope.launch{
            val imageData = imageDao.getComment(imageName)
            commentSpace.text = "${imageData.imageComment}"
        }



        button.setOnClickListener {
            val intent = Intent(this@ImageCommActivity, activity_comm_edit::class.java)
            intent.putExtra("name", imageName)
            startActivity(intent)
        }

        supportActionBar?.title = imageName
        Glide.with(this)
            .load(imagePath)
            .apply(RequestOptions().centerCrop())
            .into(findViewById(R.id.imageView))
    }


}