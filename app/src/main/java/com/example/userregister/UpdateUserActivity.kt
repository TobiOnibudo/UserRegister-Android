package com.example.userregister

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.userregister.databinding.ActivityAddUserBinding
import com.example.userregister.databinding.ActivityUpdateUserBinding

class UpdateUserActivity : AppCompatActivity() {
    lateinit var updateUserBinding: ActivityUpdateUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateUserBinding = ActivityUpdateUserBinding.inflate(layoutInflater)
        val view = updateUserBinding.root
        setContentView(view)
    }
}