package com.ilham.azurerosehealthmanagerapps

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ilham.azurerosehealthmanagerapps.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_CALORIE = "extra_calorie"
        const val EXTRA_PROTEIN = "extra_protein"
        const val EXTRA_FATS = "extra_fats"
        const val EXTRA_CARBS = "extra_carbs"
        const val EXTRA_FIBER = "extra_fiber"
    }
    /*
    @SerializedName("classify_result")
    val classifyResult: String,

    @SerializedName("energi_kalori")
    val energiKalori: Double,

    val karbohidrat: Double,
    val lemak: Double,
    val protein: Double,
    val serat: Double
     */

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val image = intent.getStringExtra(EXTRA_IMAGE)
        var name = intent.getStringExtra(EXTRA_NAME)
        name = "$name"
        var calorie = intent.getStringExtra(EXTRA_CALORIE)
        calorie = "$calorie KCal"
        var protein = intent.getStringExtra(EXTRA_PROTEIN)
        protein = "$protein g"
        var fat = intent.getStringExtra(EXTRA_FATS)
        fat = "$fat g"
        var carb = intent.getStringExtra(EXTRA_CARBS)
        carb = "$carb g"
        var fiber = intent.getStringExtra(EXTRA_FIBER)
        fiber = "$fiber g"

        binding.apply {
            imageView.setImageURI(Uri.parse(image))
            tvClassify.text = name
            tvCalorie.text = calorie
            tvProtein.text = protein
            tvFat.text = fat
            tvCarbs.text = carb
            tvFiber.text = fiber
        }
    }
}