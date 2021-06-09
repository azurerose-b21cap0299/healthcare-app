package com.ilham.azurerosehealthmanagerapps

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.ilham.azurerosehealthmanagerapps.camera.*
import com.ilham.azurerosehealthmanagerapps.databinding.FragmentCameraBinding
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraFragment : Fragment(),RequestBody.UploadCallback {
    companion object {
        private const val REQUEST_IMAGE_PICKER_CODE = 1
        private const val REQUEST_CAMERA_CODE = 2
        private const val PERMISSION_CAMERA_CODE = 102
    }

    private lateinit var binding: FragmentCameraBinding
    private lateinit var currentPhotoPath: String
    private var selectedImage: Uri? = null

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentCameraBinding.inflate(layoutInflater, container, false)
       // activity?.setContentView(binding.root)

        binding.apply {
         //   btnCamera.setOnClickListener { askCameraPermission() }
            btnGallery.setOnClickListener { openImageChooser() }
            btnUpload.setOnClickListener { uploadImage() }
        }
        // Inflate the layout for this fragment
        return binding?.root
    }
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun uploadImage() {
        if (selectedImage == null) {
            binding.root.snackbar("Select an Image First")
            return
        }

        val parcelFileDescriptor =
            activity?.contentResolver?.openFileDescriptor(selectedImage!!, "r", null) ?: return
        val file = File(activity?.cacheDir,  activity?.contentResolver?.getFileName(selectedImage!!))
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        binding.progressBar.progress = 0
        val body = RequestBody(file, "image", this)

        Api().uploadImage(
            MultipartBody.Part.createFormData("image", file.name, body)
        ).enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                binding.progressBar.progress = 100
                Log.d("upload", "classify name: {${response.body()?.toString()}}")
                Log.d("upload", "calorie: {${response.body()?.results?.energiKalori.toString()}}")

                val classifyname = response.body()?.results?.classifyResult.toString()
                val calorie = response.body()?.results?.energiKalori.toString()
                val protein = response.body()?.results?.protein.toString()
                val carb = response.body()?.results?.karbohidrat.toString()
                val fat = response.body()?.results?.lemak.toString()
                val fiber = response.body()?.results?.serat.toString()

                Intent(context, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_IMAGE, selectedImage.toString())
                    it.putExtra(DetailActivity.EXTRA_NAME, classifyname)
                    it.putExtra(DetailActivity.EXTRA_CALORIE, calorie)
                    it.putExtra(DetailActivity.EXTRA_PROTEIN, protein)
                    it.putExtra(DetailActivity.EXTRA_CARBS, carb)
                    it.putExtra(DetailActivity.EXTRA_FATS, fat)
                    it.putExtra(DetailActivity.EXTRA_FIBER, fiber)
                    startActivity(it)
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                binding.root.snackbar(t.message!!)
                Log.d("upload", "onFailure: ${t.message}")
            }
        })
    }

    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/jpg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_IMAGE_PICKER_CODE)
        }
    }

    private fun askCameraPermission() {
        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.CAMERA
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_CODE
                )
            }
        } else {
            openCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CAMERA_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(
                    context,
                    "Camera Permission is Required to User Camera",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @Throws(IOException::class)
    private fun createPhotoFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply { currentPhotoPath = absolutePath }
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { imageCaptureIntent ->
            activity?.packageManager?.let {
                imageCaptureIntent.resolveActivity(it)?.also {
                    val photoFile: File? = try {
                        createPhotoFile()
                    } catch (e: IOException) {
                        // Error occurred while creating the File
                        null
                    }

                    photoFile?.also {
                        selectedImage = context?.let { it1 ->
                            FileProvider.getUriForFile(
                                it1,
                                "com.ilham.azurerosehealthmanagerapps.fileprovider",
                                it
                            )
                        }

                        imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage)
                        startActivityForResult(imageCaptureIntent, REQUEST_CAMERA_CODE)
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_PICKER_CODE -> {
                    selectedImage = data?.data
                    binding.imageView.setImageURI(selectedImage)
                }
                REQUEST_CAMERA_CODE -> {
                    binding.imageView.setImageURI(selectedImage)
                }
            }
        }
    }

    override fun onProgressUpdate(percentage: Int) {
        binding.progressBar.progress = percentage
    }
}

