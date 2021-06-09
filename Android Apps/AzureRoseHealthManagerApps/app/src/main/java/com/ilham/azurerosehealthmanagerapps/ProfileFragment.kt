package com.ilham.azurerosehealthmanagerapps

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.ilham.azurerosehealthmanagerapps.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream


class ProfileFragment : Fragment() {

    companion object {
        const val REQUEST_CAMERA = 100
    }

    private var _fragmentProfileBinding: FragmentProfileBinding? = null
    private val binding get() = _fragmentProfileBinding
    private lateinit var imageUri : Uri

    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       _fragmentProfileBinding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser
        if (user != null) {
            if (user.photoUrl != null) {
                Picasso.get().load(user.photoUrl).into(binding?.ivProfile)
            } else {
                Picasso.get().load("https://picsum.photos/id/237/300").into(binding?.ivProfile)
            }

            binding?.etName?.setText(user.displayName)
            binding?.etEmail?.setText(user.email)

            if (user.isEmailVerified) {
                binding?.icVerified?.visibility = View.VISIBLE
            } else {
                binding?.icUnverified?.visibility = View.VISIBLE
            }

            if (user.phoneNumber.isNullOrEmpty()) {
                binding?.etPhone?.setText("Insert Phone Number")
            } else {
                binding?.etPhone?.setText(user.phoneNumber)
            }
        }

        binding?.ivProfile?.setOnClickListener {
            intentCamera()
        }

        binding?.btnUpdate?.setOnClickListener {
            val image = when {
                ::imageUri.isInitialized -> imageUri
                user?.photoUrl == null -> Uri.parse("https://picsum.photos/id/237/300")
                else -> user.photoUrl
            }

            val name = binding?.etName?.text.toString().trim()

            if (name.isEmpty()) {
                binding?.etName?.error = "Please Input Your Name"
                binding?.etName?.requestFocus()
                return@setOnClickListener
            }

            UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(image)
                .build().also {
                    user?.updateProfile(it)?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(activity, "Profile Updated", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(activity, "${it.exception?.message}",  Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
        binding?.icUnverified?.setOnClickListener {
            user?.sendEmailVerification()?.addOnCompleteListener{
                if (it.isSuccessful) {
                    Toast.makeText(activity, "Verification Email Has Been Sended", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding?.etEmail?.setOnClickListener {
            val actionUpdateEmail = ProfileFragmentDirections.actionUpdateEmail()
            Navigation.findNavController(it).navigate(actionUpdateEmail)
        }

       binding?.tvChangePassword?.setOnClickListener {
            val actionChangePassword = ProfileFragmentDirections.actionChangePassword()
           Navigation.findNavController(it).navigate(actionChangePassword)
        }



    }

    private fun intentCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {  intent ->
            activity?.packageManager?.let {
                intent.resolveActivity(it).also {
                    startActivityForResult(intent, REQUEST_CAMERA)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            var imgBitmap = data?.extras?.get("data") as Bitmap
            uploadImage(imgBitmap)
        }
    }

    private fun uploadImage(imgBitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val ref = FirebaseStorage.getInstance().reference.child("img/${FirebaseAuth.getInstance().currentUser?.uid}")

        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()

        ref.putBytes(image)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    ref.downloadUrl.addOnCompleteListener {
                        it.result?.let {
                            imageUri = it
                            binding?.ivProfile?.setImageBitmap(imgBitmap)

                        }
                    }
                }
            }
    }

}