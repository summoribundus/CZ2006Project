package com.example.tryonlysports

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Debug
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.tryonlysports.databinding.ActivityLoginBinding
import com.example.tryonlysports.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvLogin.setOnClickListener {

            onBackPressed()
        }

        binding.btnReg.setOnClickListener {
            when {

                TextUtils.isEmpty(binding.etRegEmail.text.toString().trim { it <= ' ' }) -> {
                    val text = "Please enter email."
                    val duration = Toast.LENGTH_SHORT

                    val toast = Toast.makeText(this@RegisterActivity, text, duration)
                    toast.show()
                }

                TextUtils.isEmpty(binding.etRegPassword.text.toString().trim { it <= ' ' }) -> {
                    val text = "Please enter password."
                    val duration = Toast.LENGTH_SHORT

                    val toast = Toast.makeText(this@RegisterActivity, text, duration)
                    toast.show()
                }
                else -> {

                    val email: String = binding.etRegEmail.text.toString().trim { it <= ' ' }
                    val password: String = binding.etRegPassword.text.toString().trim { it <= ' ' }

                    //Create an instance and register the user with email and password.
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->
                                //if the registration is done successfully
                                Log.i("Firebaseuser", "firebase instance get")
                                if (task.isSuccessful) {
                                    //Firebase registers the user
                                    val firebaseUser: FirebaseUser = task.result!!.user!!
                                    Log.i("Firebaseuser", "firebaseuser initialized")
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "You were registered successfully.",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val intent =
                                        Intent(this@RegisterActivity, LoginActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id", firebaseUser.uid)
                                    intent.putExtra("email_id", email)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Log.i("Firebaseuser", "firebaseuser initialized unsuccessfully")
                                    //If the registration is not successful
                                    //The application shall display an error message
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                }


            }
        }
    }

}