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

class Login : AppCompatActivity() {

    lateinit var email: EditText
    lateinit var pass: EditText
    lateinit var submit: Button
    lateinit var signup: Button
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        email = findViewById(R.id.lemail)
        pass = findViewById(R.id.lpass)
        submit = findViewById(R.id.llogin)
        signup = findViewById(R.id.lsignup)
        auth = Firebase.auth


        submit.setOnClickListener {

            auth.signInWithEmailAndPassword(email.text.toString(), pass.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("=-=-=-", "signInWithEmail:success")
                        val user = auth.currentUser
                        startActivity(Intent(this, AddProduct::class.java))
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("=-=-=-==", "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()

                    }
                }


        }
        signup.setOnClickListener {

            startActivity(Intent(this, Signup::class.java))
            finish()

        }
    }
}