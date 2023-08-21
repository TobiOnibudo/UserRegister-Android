package com.example.userregister

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.userregister.databinding.ActivityAddUserBinding


class AddUserActivity : AppCompatActivity() {
    lateinit var addUserBinding: ActivityAddUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addUserBinding = ActivityAddUserBinding.inflate(layoutInflater)
        val view = addUserBinding.root
        setContentView(view)
    }
}
