package com.example.projekt8

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.w3c.dom.Comment


@Dao
interface ImageDao {

    @Insert
    fun insert(image: Image)

    @Query("SELECT * FROM images2")
    fun getAll() : List<Image>

    @Query("SELECT * FROM images2 WHERE imagePath = :imagePath")
    fun checkFor(imagePath: String) : List<Image>

    @Query("SELECT * FROM images2 WHERE imageName = :imageName")
    suspend fun getComment(imageName: String) : Image

    @Query("UPDATE images2 SET imageComment = :imageComment WHERE imageName = :imageName")
    suspend fun updateComment(imageName: String, imageComment: String)
}