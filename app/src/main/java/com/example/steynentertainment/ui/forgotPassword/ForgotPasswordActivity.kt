package com.example.steynentertainment.ui.forgotPassword

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.steynentertainment.databinding.ActivityForgotPasswordBinding
import com.example.steynentertainment.ui.register.RegisterActivity
import com.example.steynentertainment.ui.ui.login.LoginActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

// ForgotPasswordActivity is an AppCompatActivity that handles the password reset functionality.
class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    // onCreate is called when the activity is starting.
    // This method initializes the activity, inflates its UI, and sets up listeners.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.txtLogin.setOnClickListener() {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.txtSignUp.setOnClickListener() {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.resetPassword.setOnClickListener {
            val email = binding.email.text.toString()
            sendPasswordResetEmail(email)
        }
    }

    // sendPasswordResetEmail sends a password reset email to the given email address.
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

    // isValidEmail checks if the provided email string is a valid email format.
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
