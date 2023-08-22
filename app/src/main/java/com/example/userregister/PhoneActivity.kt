package com.example.userregister

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.userregister.databinding.ActivityForgetBinding
import com.example.userregister.databinding.ActivityPhoneBinding

class PhoneActivity : AppCompatActivity() {

    lateinit var phoneBinding: ActivityPhoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        phoneBinding = ActivityPhoneBinding.inflate(layoutInflater)
        val view = phoneBinding.root
        setContentView(view)


    }
}