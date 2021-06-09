package com.ilham.azurerosehealthmanagerapps.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.RawValue
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Food (
    val foodId: String,
    val label: String,
   // val Nutrients: @RawValue Nutrients,
    val image: String,
    val kcalorie: Double,
    val protein: Double,
    val fats: Double,
    val carbs: Double,
    val fiber: Double
    ) : Parcelable