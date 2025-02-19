package com.example.lab11_kotlin.database

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Student(

    val std_id: String,

    val std_name: String,

    val std_gender: String,

    val std_age: Int
) : Parcelable {}

