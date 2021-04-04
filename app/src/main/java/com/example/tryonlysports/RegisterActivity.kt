package com.example.tryonlysports

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        tv_login.setOnClickListener {

            onBackPressed()
        }

        btn_reg.setOnClickListener {
            when {
                TextUtils.isEmpty(et_reg_email.text.toString().trim { it <= ' ' }) -> {
                    val text = "Please enter email."
                    val duration = Toast.LENGTH_SHORT

                    val toast = Toast.makeText(this@RegisterActivity, text, duration)
                    toast.show()
                }

                TextUtils.isEmpty(et_reg_password.text.toString().trim { it <= ' ' }) -> {
                    val text = "Please enter password."
                    val duration = Toast.LENGTH_SHORT

                    val toast = Toast.makeText(this@RegisterActivity, text, duration)
                    toast.show()
                }
                else -> {

                    val email: String = et_reg_email.text.toString().trim { it <= ' ' }
                    val password: String = et_reg_password.text.toString().trim { it <= ' ' }

                    //Create an instance and register the user with email and password.
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->
                                //if the registration is done successfully
                                if (task.isSuccessful) {
                                    //Firebase registers the user
                                    val firebaseUser: FirebaseUser = task.result!!.user!!

                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "You were registered successfully.",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val intent =
                                        Intent(this@RegisterActivity, MainActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id", firebaseUser.uid)
                                    intent.putExtra("email_id", email)
                                    startActivity(intent)
                                    finish()
                                } else {
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