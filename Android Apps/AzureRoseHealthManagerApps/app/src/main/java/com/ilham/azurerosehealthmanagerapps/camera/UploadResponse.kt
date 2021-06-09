package com.ilham.azurerosehealthmanagerapps.camera

import com.google.gson.annotations.SerializedName

data class UploadResponse (
        val error: String,
        val results: Results,
        val status: String
)

data class Results (
        @SerializedName("classify_result")
        val classifyResult: String,

        @SerializedName("energi_kalori")
        val energiKalori: Double,

        val karbohidrat: Double,
        val lemak: Double,
        val protein: Double,
        val serat: Double
)