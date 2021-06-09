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
import com.ilham.azurerosehealthmanagerapps.databinding.FragmentChangePasswordBinding
import com.ilham.azurerosehealthmanagerapps.databinding.FragmentUpdateEmailBinding


class ChangePasswordFragment : Fragment() {

    private var _fragmentChangePasswordBinding: FragmentChangePasswordBinding? = null
    private val binding get() = _fragmentChangePasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _fragmentChangePasswordBinding = FragmentChangePasswordBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser

        binding?.layoutPassword?.visibility = View.VISIBLE
        binding?.layoutNewPassword?.visibility = View.GONE

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
                        binding?.layoutNewPassword?.visibility = View.VISIBLE
                    } else if (it.exception is FirebaseAuthInvalidCredentialsException) {
                        binding?.etPassword?.error = "Wrong Password"
                        binding?.etPassword?.requestFocus()
                    } else {
                        Toast.makeText(activity, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            binding?.btnUpdate?.setOnClickListener { view ->
                val newPassword = binding?.etNewPassword?.text.toString()
                val newPasswordConfirm = binding?.etNewPasswordConfirm?.text.toString()

                if (newPassword.isEmpty() || newPassword.length<6) {
                    binding?.etNewPassword?.error = "Fill in your new Password"
                    binding?.etNewPassword?.requestFocus()
                    return@setOnClickListener
                }

                if (newPassword != newPasswordConfirm) {
                    binding?.etNewPasswordConfirm?.error = "Password not match"
                    binding?.etNewPasswordConfirm?.requestFocus()
                    return@setOnClickListener
                }

                user?.let {
                    user.updatePassword(newPassword).addOnCompleteListener{
                        if (it.isSuccessful){
                            val actionPasswordChanged = ChangePasswordFragmentDirections.actionPasswordChange()
                           Navigation.findNavController(view).navigate(actionPasswordChanged)
                            Toast.makeText(activity, "Password sucessfully changed", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(activity, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        }
    }


}