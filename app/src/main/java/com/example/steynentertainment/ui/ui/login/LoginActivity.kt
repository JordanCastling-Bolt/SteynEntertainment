package com.example.steynentertainment.ui.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.steynentertainment.MainActivity
import com.example.steynentertainment.R
import com.example.steynentertainment.databinding.ActivityLoginBinding
import com.example.steynentertainment.ui.forgotPassword.ForgotPasswordActivity
import com.example.steynentertainment.ui.register.RegisterActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val username = binding.txtEmail
        val password = binding.password
        val login = binding.login
        val loading = binding.loading

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id)) // Replace with your web client ID
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.googleFab?.setOnClickListener {
            signIn(googleSignInClient)
        }

        binding.forgotPassword?.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        binding.signUp?.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(
            this@LoginActivity,
            Observer {
                val loginState = it ?: return@Observer

                // disable login button unless both username / password is valid
                login.isEnabled = loginState.isDataValid

                if (loginState.usernameError != null) {
                    username.error = getString(loginState.usernameError)
                }
                if (loginState.passwordError != null) {
                    password.error = getString(loginState.passwordError)
                }
            }
        )

        loginViewModel.loginResult.observe(
            this@LoginActivity,
            Observer {
                val loginResult = it ?: return@Observer

                // Hide ProgressBar
                binding.loading.visibility = View.GONE

                if (loginResult.error != null) {
                    showLoginFailed(loginResult.error)
                }
                if (loginResult.success != null) {
                    // Show ProgressBar
                    binding.loading.visibility = View.VISIBLE

                    updateUiWithUser(loginResult.success)
                    setResult(Activity.RESULT_OK)
                    // Check user role and navigate to the corresponding MainActivity
                    checkUserRoleAndNavigate(auth.currentUser!!.uid)
                }
            }
        )

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            this.context, // passing context
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(
                    this.context, // passing context
                    username.text.toString(),
                    password.text.toString()
                )
            }
        }
    }
    private fun checkUserRoleAndNavigate(uid: String) {
        val docRef = db.collection("Users").document(uid)
        docRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val role = document.getString("role")
                if (role != null) {
                    navigateToActivityBasedOnRole(role)
                } else {
                    // Handle the case where the "role" field is not present in the Firestore document
                    // For example, you could navigate to a default activity or show an error message.
                }
            } else {
                // Handle missing document case
                // For example, you could show an error message.
            }
        }.addOnFailureListener {
            // Handle error
            // For example, you could log the error or show an error message.
        }
    }

    private fun navigateToActivityBasedOnRole(role: String) {
        // Show ProgressBar
        binding.loading.visibility = View.VISIBLE

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("IS_MEMBER", role == "member")

        // Add an extra for full access
        intent.putExtra("LIMITED_ACCESS", false)

        startActivity(intent)

        // Hide ProgressBar
        binding.loading.visibility = View.GONE
    }

    private fun signIn(googleSignInClient: GoogleSignInClient) {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign-In failed
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Show ProgressBar
                    binding.loading.visibility = View.VISIBLE

                    // Successfully signed in
                    val user = auth.currentUser
                    checkIfUserExistsOrCreate(user!!.uid, acct) // new function
                } else {
                    // Sign-in failed
                    showLoginFailed(R.string.login_failed)
                }
            }
    }

    // New function to check if the user already exists or create a new one if not
    private fun checkIfUserExistsOrCreate(uid: String, acct: GoogleSignInAccount) {
        val docRef = db.collection("Users").document(uid)
        docRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                // User exists, proceed to navigate
                val role = document.getString("role")
                if (role != null) {
                    navigateToActivityBasedOnRole(role)
                }
            } else {
                // User doesn't exist, create a new document
                val newUser = hashMapOf(
                    "email" to acct.email,
                    "firstName" to acct.givenName,
                    "lastName" to acct.familyName,
                    "role" to "user",
                    "terms_accepted" to false,
                    "yearlyPayments" to 0,
                    "subscribed" to "no"
                )
                db.collection("Users").document(uid).set(newUser).addOnSuccessListener {
                    // Successfully created new user, now proceed to navigate
                    val role = document.getString("role")
                    if (role != null) {
                        navigateToActivityBasedOnRole(role)
                    }
                }.addOnFailureListener { e ->
                    // Handle failure
                }
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName

        // Initiate successful logged-in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
        // Complete and destroy login activity
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
