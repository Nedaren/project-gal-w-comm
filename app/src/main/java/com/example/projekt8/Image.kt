package com.example.projekt8

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images2" )
data class Image (@PrimaryKey(true) val id: Int? = null,
                  val imagePath: String,
                  val imageName: String,
                  var imageComment: String?
)