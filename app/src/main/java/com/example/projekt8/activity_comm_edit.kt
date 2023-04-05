package com.example.projekt8

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.GlobalScope

private lateinit var database: AppDatabase
private lateinit var imageDao: ImageDao

class activity_comm_edit : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comm_edit)

        val imageName = intent.getStringExtra("name").toString()
        supportActionBar?.title = imageName

        val updt = findViewById<Button>(R.id.update_button)
        val del = findViewById<Button>(R.id.delete_button)
        val edit = findViewById<EditText>(R.id.photo_commentary)


        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app_database"
        ).createFromAsset("/data/data/com.example.project8/databases").build()

        imageDao = database.imageDao()


        updt.setOnClickListener {
            val comment = edit.text.toString()

            lifecycleScope.launch{
                database.imageDao().updateComment(imageName, comment)
            }
            finish()
        }

        del.setOnClickListener {
            lifecycleScope.launch{
                database.imageDao().updateComment(imageName, " ")
            }
            finish()
        }
    }
}