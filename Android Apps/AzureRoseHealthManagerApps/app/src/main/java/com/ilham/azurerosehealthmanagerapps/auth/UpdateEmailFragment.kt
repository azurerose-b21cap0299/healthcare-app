package com.ilham.azurerosehealthmanagerapps.auth

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.ilham.azurerosehealthmanagerapps.R
import com.ilham.azurerosehealthmanagerapps.databinding.FragmentUpdateEmailBinding


class UpdateEmailFragment : Fragment() {

    private var _fragmentUpdateEmailBinding: FragmentUpdateEmailBinding? = null
    private val binding get() = _fragmentUpdateEmailBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _fragmentUpdateEmailBinding = FragmentUpdateEmailBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser

        binding?.layoutPassword?.visibility = View.VISIBLE
        binding?.layoutEmail?.visibility = View.GONE

        binding?.btnAuth?.setOnClickListener {
            val password = binding?.etPassword?.text.toString().trim()

            if (password.isEmpty()) {
                binding?.etPassword?.error = "Please input New Password"
                binding?.etPassword?.requestFocus()
                return@setOnClickListener
            }

            user?.let {
                val userCredential = EmailAuthProvider.getCredential(it.email!!, password)
                it.reauthenticate(userCredential).addOnCompleteListener {
                    if (it.isSuccessful) {
                        binding?.layoutPassword?.visibility = View.GONE
                        binding?.layoutEmail?.visibility = View.VISIBLE
                    } else if (it.exception is FirebaseAuthInvalidCredentialsException) {
                        binding?.etPassword?.error = "Wrong Password"
                        binding?.etPassword?.requestFocus()
                    } else {
                        Toast.makeText(activity, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            binding?.btnUpdate?.setOnClickListener { view ->
                val email = binding?.etEmail?.text.toString()
                if (email.isEmpty()) {
                    binding?.etEmail?.error = "Fill in your email"
                    binding?.etEmail?.requestFocus()
                    return@setOnClickListener
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    binding?.etEmail?.error = "Email is not valid"
                    binding?.etEmail?.requestFocus()
                    return@setOnClickListener
                }

                user?.let {
                    user.updateEmail(email).addOnCompleteListener{
                        if (it.isSuccessful){
                            val actionEmailUpdated = UpdateEmailFragmentDirections.actionEmailUpdated()
                            Navigation.findNavController(view).navigate(actionEmailUpdated)
                        } else {
                            Toast.makeText(activity, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        }
    }


}