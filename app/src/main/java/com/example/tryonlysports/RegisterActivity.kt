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
import androidx.annotation.WorkerThread
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.ArrayList
import java.util.Date
import java.util.HashMap
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * This is the Activity for registering users
 *
 * @author LI YIBAI
 */
class RegisterActivity : AppCompatActivity() {

    /**
     * This is the binding of the activity_register layout
     */
    private lateinit var binding: ActivityRegisterBinding

    /**
     * This is the function of setting up layouts, buttons, and text input fields
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method of MainActivity.
     * @return a View to display on the Register page.
     */
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

                TextUtils.isEmpty(binding.etRegPhone.text.toString().trim { it <= ' ' }) -> {
                    val text = "Please enter your phone number."
                    val duration = Toast.LENGTH_SHORT

                    val toast = Toast.makeText(this@RegisterActivity, text, duration)
                    toast.show()
                }

                TextUtils.isEmpty(binding.etRegRegion.text.toString().trim { it <= ' ' }) -> {
                    val text = "Please enter your country/region."
                    val duration = Toast.LENGTH_SHORT

                    val toast = Toast.makeText(this@RegisterActivity, text, duration)
                    toast.show()
                }

                TextUtils.isEmpty(binding.etRegUsername.text.toString().trim { it <= ' ' }) -> {
                    val text = "Please enter your username."
                    val duration = Toast.LENGTH_SHORT

                    val toast = Toast.makeText(this@RegisterActivity, text, duration)
                    toast.show()
                }

                else -> {

                    val email: String = binding.etRegEmail.text.toString().trim { it <= ' ' }
                    val password: String = binding.etRegPassword.text.toString().trim { it <= ' ' }
                    val phoneNumber: String = binding.etRegPhone.text.toString().trim { it <= ' ' }
                    val region: String = binding.etRegRegion.text.toString().trim { it <= ' ' }
                    val username: String = binding.etRegUsername.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->
                                //if the registration is done successfully
                                Log.i("Firebaseuser", "firebase instance get")
                                if (task.isSuccessful) {
                                    //Firebase registers the user
                                    val firebaseUser: FirebaseUser = task.result!!.user!!
                                    Log.i("Firebaseuser", "firebaseuser initialized")
                                    // Write personal information to the firestore database
                                    val db = Firebase.firestore
                                    val settings = firestoreSettings {
                                        isPersistenceEnabled = true
                                    }
                                    db.firestoreSettings = settings
                                    val newUser = hashMapOf(
                                        "phoneNumber" to phoneNumber,
                                        "region" to region,
                                        "userEmail" to email,
                                        "username" to username
                                    )
                                    db.collection("user").document(email)
                                        .set(newUser, SetOptions.merge())
                                    //Give a success toast message to users
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "You were registered successfully. Now please login...",
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