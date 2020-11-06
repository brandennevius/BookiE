package com.example.bookiefinal

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        dataManager = ViewModelProviders.of(this).get(DataManager::class.java)
        dataManager.getTheOddsApiInfo()


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_bet_history, R.id.nav_leagues,
                R.id.nav_teams, R.id.nav_share, R.id.nav_send
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    class LoginActivity : AppCompatActivity() {
        private lateinit var appBarConfiguration: AppBarConfiguration
        lateinit var dataManager: DataManager

        private val TAG = "LoginActivity"
        //global variables
        private var email: String? = null
        private var password: String? = null
        //UI elements
        private var tvForgotPassword: TextView? = null
        private var etEmail: EditText? = null
        private var etPassword: EditText? = null
        private var btnLogin: Button? = null
        private var btnCreateAccount: Button? = null
        private var btnForgotPassword: Button? = null

        //Firebase references
        private var mAuth: FirebaseAuth? = null


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_login)
            initialise()
        }
        private fun initialise() {
            //tvForgotPassword = findViewById<View>(R.id.tv_forgot_password) as TextView
            etEmail = findViewById<View>(R.id.email_login) as EditText
            etPassword = findViewById<View>(R.id.passwrod_login) as EditText
            btnLogin = findViewById<View>(R.id.loginButton) as Button
            btnCreateAccount = findViewById<View>(R.id.CreateAccountBtn) as Button
            btnForgotPassword = findViewById<View>(R.id.forgotPasswordBtn) as Button
            //mProgressBar = ProgressDialog(this)
            mAuth = FirebaseAuth.getInstance()

            btnForgotPassword!!.setOnClickListener {
                startActivity(
                    Intent(this@LoginActivity,
                        ForgotPasswordActivity::class.java)
                )
            }
            btnCreateAccount!!
                .setOnClickListener { startActivity(
                    Intent(this@LoginActivity,
                        CreateAccount::class.java)
                ) }
            btnLogin!!.setOnClickListener {
                loginUser()
            }
        }


        private fun loginUser() {
            email = etEmail?.text.toString()

            password = etPassword?.text.toString()
            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                Toast.makeText(this@LoginActivity, "Loging in...", Toast.LENGTH_LONG).show()
                Log.d(TAG, "Logging in user.")
                mAuth!!.signInWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            dataManager.username.value = email
                            updateUI()
                            Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_LONG).show()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(this@LoginActivity, "Invalid Username or Password",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
            }
        }
        private fun updateUI() {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    class ForgotPasswordActivity : AppCompatActivity() {

        private val TAG = "ForgotPasswordActivity"
        //UI elements
        private var etEmail: EditText? = null
        private var btnSubmit: Button? = null
        //Firebase references
        private var mAuth: FirebaseAuth? = null


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_forgot_password)
            initialise()
        }
        private fun initialise() {
            etEmail = findViewById<View>(R.id.et_email) as EditText
            btnSubmit = findViewById<View>(R.id.btn_submit) as Button
            mAuth = FirebaseAuth.getInstance()
            btnSubmit!!.setOnClickListener { sendPasswordResetEmail() }
        }

        private fun sendPasswordResetEmail() {
            val email = etEmail?.text.toString()
            if (!TextUtils.isEmpty(email)) {
                mAuth!!
                    .sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val message = "Email sent."
                            Log.d(TAG, message)
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            updateUI()
                        } else {
                            Log.w(TAG, task.exception!!.message)
                            Toast.makeText(this, "No user found with this email.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show()
            }
        }
        private fun updateUI() {
            val intent = Intent(this@ForgotPasswordActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    class CreateAccount : AppCompatActivity() {


        var database = MutableLiveData<DatabaseReference>()

        //UI elements
        private var etFirstName: EditText? = null
        private var etLastName: EditText? = null
        private var etEmail: EditText? = null
        private var etPassword: EditText? = null
        private var btnCreateAccount: Button? = null
//        private var mProgressBar: ProgressDialog? = null

        //Firebase references
        private var mDatabaseReference: DatabaseReference? = null
        private var mDatabase: FirebaseDatabase? = null
        private var mAuth: FirebaseAuth? = null

        private val TAG = "CreateAccountActivity"

        //global variables
        private var firstName: String? = null
        private var lastName: String? = null
        private var email: String? = null
        private var password: String? = null


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_create_account)
            initialise()

        }

        private fun initialise() {
            etFirstName = findViewById<View>(R.id.firstName) as EditText
            etLastName = findViewById<View>(R.id.lastName) as EditText
            etEmail = findViewById<View>(R.id.email) as EditText
            etPassword = findViewById<View>(R.id.password) as EditText
            btnCreateAccount = findViewById<View>(R.id.register) as? Button

            mDatabase = FirebaseDatabase.getInstance()
            mDatabaseReference = mDatabase!!.reference!!.child("Users")
            mAuth = FirebaseAuth.getInstance()
            btnCreateAccount!!.setOnClickListener { createNewAccount() }
        }

        private fun createNewAccount() {
            firstName = etFirstName?.text.toString()
            lastName = etLastName?.text.toString()
            email = etEmail?.text.toString()
            password = etPassword?.text.toString()

            if (!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName)
                && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)
            ) {
                Toast.makeText(this, "Registering User...", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
            }

            mAuth!!
                .createUserWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(this) { task ->
                    //
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@CreateAccount,
                            "Successfully Created Account",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val userId = mAuth!!.currentUser!!.uid
                        //Verify Email
                        verifyEmail()
                        //update user profile information
                        val currentUserDb = mDatabaseReference!!.child(userId)
                        currentUserDb.child("firstName").setValue(firstName)
                        currentUserDb.child("lastName").setValue(lastName)
                        updateUserInfoAndUI()
                    } else {
                        Toast.makeText(
                            this@CreateAccount,
                            "Failed to Register User",
                            Toast.LENGTH_SHORT
                        ).show()
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            this@CreateAccount, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        private fun updateUserInfoAndUI() {
            //start next activity
            val intent = Intent(this@CreateAccount, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        private fun verifyEmail() {
            val mUser = mAuth!!.currentUser
            mUser!!.sendEmailVerification()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@CreateAccount,
                            "Verification email sent to " + mUser.getEmail(),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Log.e(TAG, "sendEmailVerification", task.exception)
                        Toast.makeText(
                            this@CreateAccount,
                            "Failed to send verification email.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }


    }

}
