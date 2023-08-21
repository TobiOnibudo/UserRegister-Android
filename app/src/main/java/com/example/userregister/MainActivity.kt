package com.example.userregister

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userregister.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding : ActivityMainBinding

    private val database : FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference : DatabaseReference  = database.reference.child("MyUsers")

    private val userList = ArrayList<Users>()
    private lateinit var usersAdapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        mainBinding.floatingActionButton.setOnClickListener {
            val intent = Intent(this,AddUserActivity::class.java)
            startActivity(intent)
        }
        retrieveDataFromDatabase()

    }

    private fun retrieveDataFromDatabase(){

        myReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
             userList.clear()
               for( eachUser in snapshot.children)
               {
                   val user = eachUser.getValue(Users::class.java)

                   if (user != null)
                   {
                        println("userId: ${user.userId}")
                        println("userName: ${user.userName}")
                        println("userAge: ${user.userAge}")
                        println("userEmail: ${user.userEmail}")
                       println("*********************************")

                       userList.add(user)
                   }

                   usersAdapter = UsersAdapter(this@MainActivity,userList)

                   mainBinding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

                   mainBinding.recyclerView.adapter = usersAdapter
               }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}