package com.example.userregister

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.userregister.databinding.ActivityForgetBinding
import com.google.firebase.auth.FirebaseAuth

class ForgetActivity : AppCompatActivity() {

    lateinit var forgetBinding : ActivityForgetBinding

    val auth : FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgetBinding = ActivityForgetBinding.inflate(layoutInflater)
        val view = forgetBinding.root

        setContentView(view)

        forgetBinding.buttonReset.setOnClickListener {
            val email = forgetBinding.editTextReset.text.toString()

            auth.sendPasswordResetEmail(email).addOnCompleteListener {
                task->
                if (task.isSuccessful)
                {
                    Toast.makeText(applicationContext,
                        "We've sent a password reset email to you",
                        Toast.LENGTH_LONG).show()

                    finish()
                }
                else
                {
                    Toast.makeText(applicationContext,
                        task.exception?.toString(),
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}