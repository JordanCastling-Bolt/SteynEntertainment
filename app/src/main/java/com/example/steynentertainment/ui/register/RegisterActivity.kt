package com.example.steynentertainment.ui.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.steynentertainment.R
import com.example.steynentertainment.databinding.ActivityRegisterBinding
import com.example.steynentertainment.ui.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.example.steynentertainment.ui.register.RegisterResult


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab2.setOnClickListener {
            finish()
        }
        val firebaseAuth = FirebaseAuth.getInstance()
        val factory = RegisterViewModelFactory(firebaseAuth)

        val usernameEditText = binding.txtEmail
        val passwordEditText = binding.password
        val confirmPasswordEditText = binding.retypePassword
        val firstNameEditText = binding.txtFirstName
        val lastNameEditText = binding.txtLastName
        val signUpButton = binding.login

        signUpButton.setOnClickListener {
            val email = usernameEditText.text.toString()
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val password = passwordEditText.text.toString()

            registerViewModel.register(email, firstName, lastName, password)
        }

        registerViewModel = ViewModelProvider(this, factory).get(RegisterViewModel::class.java)

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
        }) // End of registerFormState observer

        registerViewModel.registerResult.observe(this@RegisterActivity, Observer {
            val registerResult = it ?: return@Observer

            when (registerResult) {
                is RegisterResult.Success -> {
                    Toast.makeText(
                        applicationContext,
                        "Registration was successful! Please verify your email.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                is RegisterResult.Error -> {
                    Toast.makeText(
                        applicationContext,
                        getString(registerResult.error),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })

        registerViewModel.navigateToLogin.observe(this@RegisterActivity, Observer { navigate ->
            if (navigate) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
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
