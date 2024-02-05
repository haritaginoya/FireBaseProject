package com.note.firebase2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Signup : AppCompatActivity() {
    lateinit var email: EditText
    lateinit var pass: EditText
    lateinit var signup: Button
    lateinit var login: Button

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        email = findViewById(R.id.email)
        pass = findViewById(R.id.pass)
        signup = findViewById(R.id.signup)
        login = findViewById(R.id.login)

        auth = Firebase.auth


        signup.setOnClickListener {

            auth.createUserWithEmailAndPassword(email.text.toString(), pass.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("=====", "createUserWithEmail:success")
                        val user = auth.currentUser
                        startActivity(Intent(this, Login::class.java))
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("-=-=-=", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }


        }
        login.setOnClickListener {

            startActivity(Intent(this, Login::class.java))
            finish()

        }
    }
}