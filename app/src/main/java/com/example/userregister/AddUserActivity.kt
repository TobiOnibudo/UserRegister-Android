package com.example.userregister

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import com.example.userregister.databinding.ActivityAddUserBinding
import com.google.android.play.integrity.internal.ac
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso


class AddUserActivity : AppCompatActivity() {
    lateinit var addUserBinding: ActivityAddUserBinding

    val database : FirebaseDatabase = FirebaseDatabase.getInstance()
    val myReference : DatabaseReference = database.reference.child("MyUsers")

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    var imageUri : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addUserBinding = ActivityAddUserBinding.inflate(layoutInflater)
        val view = addUserBinding.root
        setContentView(view)

        //register
        registerActivityForResult()

        supportActionBar?.title = "Add User"
        addUserBinding.buttonAddUser.setOnClickListener {
            addUserToDatabase()
        }

        addUserBinding.userProfileImage.setOnClickListener {
            chooseImage()
        }
    }

    private fun registerActivityForResult() {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),ActivityResultCallback{ result ->
            val resultCode = result.resultCode
            val imageData = result.data

            if(resultCode == RESULT_OK && imageData != null)
            {
                imageUri = imageData.data

                //Picasso
                imageUri?.let {
                    Picasso.get().load(it).into(addUserBinding.userProfileImage)
                }
            }
        })
    }

    private fun chooseImage() {
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }
        else{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            activityResultLauncher.launch(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            activityResultLauncher.launch(intent)
        }

    }

    fun addUserToDatabase()
    {
        val name : String = addUserBinding.editTextName.text.toString()
        val age : Int = addUserBinding.editTextAge.text.toString().toInt()
        val email : String = addUserBinding.editTextEmail.text.toString()

        val id : String = myReference.push().key.toString()

        val user = Users(id,name,age,email)

        myReference.child(id).setValue(user).addOnCompleteListener {
            task->
            if (task.isSuccessful)
            {
                Toast.makeText(applicationContext,
                    "The new user has been added to the database",
                    Toast.LENGTH_SHORT).show()

                finish()
            }
            else
            {
                Toast.makeText(applicationContext,
                    task.exception.toString(),
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}
