package com.example.vibespotapplication.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vibespotapplication.Activities.LoginActivity
import com.example.vibespotapplication.Activities.MainActivity
import com.example.vibespotapplication.R
import com.example.vibespotapplication.databinding.ActivityLoginBinding
import com.example.vibespotapplication.databinding.ActivitySignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize
        mAuth = FirebaseAuth.getInstance()
        db = Firebase.firestore


        // click on back btn
        binding.ivBack.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
        }


        //click on signUp btn
        binding.btnSignUp.setOnClickListener {

            val name = binding.etSignName.text.toString()
            val email = binding.etSignEmail.text.toString()
            val pwd = binding.etSignPwd.text.toString()
            signUp(name, email, pwd)
        }


    }


    //sign up fun
    private fun signUp(name: String, email: String, pwd: String) {
        mAuth.createUserWithEmailAndPassword(email, pwd)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val currentUser = mAuth.currentUser
                    currentUser?.let { user ->
                        addUserToDatabase(name, email, user.uid)
                    } ?: run {

                        Toast.makeText(this, "User created but UID not found.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorMessage = task.exception?.message ?: "Some error occurred during sign up."
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
    }


    // add data to the cloud firestore
    private fun addUserToDatabase(name:String, email: String, uid: String){

        val user = hashMapOf(
            "uid" to uid,
            "name" to name,
            "email" to email,
            "joinedAt" to System.currentTimeMillis()
        )


        db.collection("users")
            .document(uid)
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(this, "User signed up successfully", Toast.LENGTH_SHORT).show()
                val intent=Intent(this, MainActivity::class.java)
                finish()
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.d("failed to store user data", "addUserToDatabase: ${it.message}")
                Toast.makeText(this, "Failed to SignUP: ${it.message}", Toast.LENGTH_SHORT).show()
            }

    }




}


