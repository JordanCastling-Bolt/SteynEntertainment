package com.example.steynentertainment.ui.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.steynentertainment.databinding.ActivityRegisterBinding
import com.example.steynentertainment.ui.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firebaseAuth = FirebaseAuth.getInstance()
        val factory = RegisterViewModelFactory(firebaseAuth)

        progressBar = binding.loading

        val usernameEditText = binding.txtEmail
        val passwordEditText = binding.password
        val confirmPasswordEditText = binding.retypePassword
        val firstNameEditText = binding.txtFirstName
        val lastNameEditText = binding.txtLastName
        val signUpButton = binding.login
        val termsAndConditionsCheckBox = binding.termsAndConditionsCheckBox
        val txtLogin = binding.txtLogin

        val termsAndConditionsText = binding.termsAndConditionsText
        termsAndConditionsText.setOnClickListener {
            // Initialize an AlertDialog Builder
            val builder = AlertDialog.Builder(this)

            // Set dialog properties
            builder.setTitle("Terms and Conditions")
            builder.setMessage(
                "Steyn Entertainment - Terms and Conditions\n" +
                    "\n" +
                    "Last Updated: October 30, 2023\n" +
                    "\n" +
                    "1. Introduction\n" +
                    "----------------\n" +
                    "Welcome to Steyn Entertainment. By signing up for our service, you agree to comply with these terms. Please read them carefully before proceeding.\n" +
                    "\n" +
                    "2. Service Description\n" +
                    "-----------------------\n" +
                    "Steyn Entertainment provides a platform for entertainment content, including but not limited to movies, music, and live events.\n" +
                    "\n" +
                    "3. Privacy Policy\n" +
                    "------------------\n" +
                    "Your privacy is important to us. Please review our Privacy Policy to understand how we collect and handle your personal information.\n" +
                    "\n" +
                    "4. Account Responsibilities\n" +
                    "----------------------------\n" +
                    "You are responsible for maintaining the confidentiality of your account credentials. Steyn Entertainment will not be liable for any loss arising from unauthorized use of your account.\n" +
                    "\n" +
                    "5. Content Policy\n" +
                    "------------------\n" +
                    "You are not permitted to upload, share, or distribute content that is illegal, harmful, or offensive. Steyn Entertainment reserves the right to remove such content and terminate accounts involved in such activities.\n" +
                    "\n" +
                    "6. Limitation of Liability\n" +
                    "---------------------------\n" +
                    "Steyn Entertainment will not be liable for any direct, indirect, or incidental damages arising from the use or inability to use the service.\n" +
                    "\n" +
                    "7. Modification of Terms\n" +
                    "-------------------------\n" +
                    "Steyn Entertainment reserves the right to change or modify these terms at any time. Changes will be effective immediately upon posting on our platform.\n" +
                    "\n" +
                    "8. Termination\n" +
                    "--------------\n" +
                    "Steyn Entertainment reserves the right to terminate your account for breach of these terms or any illegal activities.\n" +
                    "\n" +
                    "9. Governing Law\n" +
                    "-----------------\n" +
                    "These terms will be governed by the laws of the jurisdiction in which Steyn Entertainment operates.\n" +
                    "\n" +
                    "10. Contact Information\n" +
                    "------------------------\n" +
                    "For questions or concerns regarding these terms, please contact us at [Your Contact Information].\n"
            )
            builder.setPositiveButton("Accept") { dialog, _ ->
                dialog.dismiss()
                // Set the checkbox to checked
                termsAndConditionsCheckBox.isChecked = true
            }
            builder.setNegativeButton("Decline") { dialog, _ ->
                dialog.dismiss()
                // Optionally, you can uncheck the checkbox here if you want
                termsAndConditionsCheckBox.isChecked = false
            }

            // Show the dialog
            val dialog = builder.create()
            dialog.show()
        }

        txtLogin.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        signUpButton.setOnClickListener {
            // Check if terms and conditions are accepted
            val areTermsAccepted = termsAndConditionsCheckBox.isChecked
            if (!areTermsAccepted) {
                Toast.makeText(
                    applicationContext,
                    "You must accept the Terms and Conditions to proceed",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            // Existing logic for registration
            progressBar.visibility = View.VISIBLE
            val email = usernameEditText.text.toString()
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val password = passwordEditText.text.toString()
            registerViewModel.register(email, firstName, lastName, password)
        }

        registerViewModel = ViewModelProvider(this, factory).get(RegisterViewModel::class.java)

        registerViewModel.registerFromState.observe(
            this@RegisterActivity,
            Observer {
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
            }
        ) // End of registerFromState observer

        registerViewModel.registerResult.observe(
            this@RegisterActivity,
            Observer {
                val registerResult = it ?: return@Observer

                // Hide progress bar when result received
                progressBar.visibility = View.GONE

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
            }
        )

        registerViewModel.navigateToLogin.observe(
            this@RegisterActivity,
            Observer { navigate ->
                if (navigate) {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        )

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
