package com.example.vibespotapplication.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vibespotapplication.Activities.MainActivity
import com.example.vibespotapplication.R
import com.example.vibespotapplication.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        // inflate signUpActivity on click
        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }


        // click on login btn
        binding.btnLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString()
            val pwd = binding.etLoginPwd.text.toString()
            login(email, pwd)
        }

    }


    //check user logged in or not
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in.
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }


    //logged in user
    private fun login(email: String, pwd: String) {
        mAuth.signInWithEmailAndPassword(email, pwd)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // jump to home activity
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    Toast.makeText(this, "User not exists", Toast.LENGTH_SHORT).show()
                }
            }
    }





}

