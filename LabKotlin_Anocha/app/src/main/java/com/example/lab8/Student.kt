package com.example.lab8

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Student(
    @Expose
    @SerializedName("std_id") val std_id : String,

    @Expose
    @SerializedName("std_name") val std_name : String,

    @Expose
    @SerializedName("std_gender") val std_gender : String,

    @Expose
    @SerializedName("std_age") val std_age : Int)
