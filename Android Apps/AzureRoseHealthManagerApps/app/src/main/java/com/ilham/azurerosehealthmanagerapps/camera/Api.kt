package com.ilham.azurerosehealthmanagerapps.camera

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Api {


    companion object {
        operator fun invoke() : Api {
            return Retrofit.Builder()
                    .baseUrl("http://34.101.241.91:5000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(Api::class.java)
        }
    }

    @Multipart
    @POST("/predict")
    fun uploadImage(
            @Part image: MultipartBody.Part, // File
    ): Call<UploadResponse>
}