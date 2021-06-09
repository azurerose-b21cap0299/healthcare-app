package com.ilham.azurerosehealthmanagerapps.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.ilham.azurerosehealthmanagerapps.R
import com.ilham.azurerosehealthmanagerapps.databinding.ActivityLoginBinding
import com.ilham.azurerosehealthmanagerapps.databinding.ActivityResetPasswordBinding

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.btnReset.setOnClickListener {
            val email = binding.etEmailAddress.text.toString().trim()
            if (email.isEmpty()) {
                binding.etEmailAddress.error = "Fill in your email"
                binding.etEmailAddress.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.etEmailAddress.error = "Email is not valid"
                binding.etEmailAddress.requestFocus()
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener{
                if (it.isSuccessful){
                    Toast.makeText(this, "Check your email for reset password", Toast.LENGTH_SHORT).show()
                    Intent(this@ResetPasswordActivity, LoginActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                    }
                }else {
                    Toast.makeText(this, "${it.exception?.message}" , Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}