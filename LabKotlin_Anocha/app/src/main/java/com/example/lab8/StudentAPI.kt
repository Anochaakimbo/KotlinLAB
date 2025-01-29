package com.example.lab8


import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface StudentAPI {
    @GET("allStd")
    fun retrieveStudent(): Call<List<Student>>

    @FormUrlEncoded
    @POST("std")
    fun insertStd(
        @Field("std_id") std_id: String,
        @Field("std_name") std_name: String,
        @Field("std_gender") std_gender: String,
        @Field("std_age") std_age: String
    )
    : Call<Student>

    companion object {
        fun create(): StudentAPI {
            val stdClient: StudentAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(StudentAPI::class.java)
            return stdClient
        }
    }
}