package com.example.steynentertainment.ui.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.steynentertainment.R
import com.example.steynentertainment.databinding.ActivityRegisterBinding
import com.example.steynentertainment.ui.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab2.setOnClickListener {
            finish()
        }
        val factory = RegisterViewModelFactory()

        val usernameEditText = binding.username // Change these IDs to match your layout
        val passwordEditText = binding.password
        val confirmPasswordEditText = binding.retypePassword
        val signUpButton = binding.login // Change the ID to your Sign Up button

        registerViewModel = ViewModelProvider(this, factory).get(RegisterViewModel::class.java)

        // Observe form state changes
        registerViewModel.registerFormState.observe(this@RegisterActivity, Observer {
            val registerState = it ?: return@Observer

            // Enable or disable the sign-up button
            signUpButton.isEnabled = registerState.isDataValid

            if (registerState.usernameError != null) {
                usernameEditText.error = getString(registerState.usernameError)
            }
            if (registerState.passwordError != null) {
                passwordEditText.error = getString(registerState.passwordError)
            }
            if (registerState.confirmPasswordError != null) {
                confirmPasswordEditText.error = getString(registerState.confirmPasswordError)
            }
        })

        val afterTextChangedListener = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                registerViewModel.registerDataChanged(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString(),
                    confirmPasswordEditText.text.toString()
                )
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        confirmPasswordEditText.addTextChangedListener(afterTextChangedListener)
    }
}


