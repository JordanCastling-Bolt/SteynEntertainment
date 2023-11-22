package com.example.steynentertainment.ui.forgotPassword

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.steynentertainment.databinding.ActivityForgotPasswordBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener {
            finish()
        }

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val email = binding.email.text.toString()
                val confirmEmail = binding.retypeEmail.text.toString()

                // Enable the button only if both email fields contain a valid email and are identical
                binding.resetPassword.isEnabled = isValidEmail(email) && email == confirmEmail
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
            }
        }
        binding.email.addTextChangedListener(textWatcher)
        binding.retypeEmail.addTextChangedListener(textWatcher)

        binding.resetPassword.setOnClickListener {
            val email = binding.email.text.toString()
            sendPasswordResetEmail(email)
        }
    }
    private fun sendPasswordResetEmail(email: String) {
        val auth = FirebaseAuth.getInstance()

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Snackbar.make(binding.coordinatorLayout, "Password reset link sent to your email.", Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(binding.coordinatorLayout, "Failed to send password reset link.", Snackbar.LENGTH_LONG).show()
                }
            }
    }
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
