package com.example.userregister

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.userregister.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding : ActivityMainBinding

    private val database : FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myReference : DatabaseReference  = database.reference.child("MyUsers")

    private val userList = ArrayList<Users>()
    private lateinit var usersAdapter: UsersAdapter

    val firebaseStorage : FirebaseStorage = FirebaseStorage.getInstance()
    val storageReference : StorageReference = firebaseStorage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        mainBinding.floatingActionButton.setOnClickListener {
            val intent = Intent(this,AddUserActivity::class.java)
            startActivity(intent)
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                  val id = usersAdapter.getUserId(viewHolder.adapterPosition)

                myReference.child(id).removeValue()

                //delete()
                val imageName = usersAdapter.getImageName(viewHolder.adapterPosition)

                val imageReference = storageReference.child("images").child(imageName)

                imageReference.delete()

                Toast.makeText(applicationContext,"The user has been deleted",Toast.LENGTH_SHORT).show()
            }

        }).attachToRecyclerView(mainBinding.recyclerView)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_delete_all,menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.deleteAll)
        {
            showDialogMessage()
        }else if (item.itemId == R.id.signOut)
        {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@MainActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()

        }

        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun showDialogMessage(){
        val dialogMessage = AlertDialog.Builder(this)
        dialogMessage.setTitle("Delete All")
        dialogMessage.setMessage("If Yes is clicked,all users will be deleted, if you want to delete a specific user," +
                " you can swipe the item you want to delete right or left" )
        dialogMessage.setNegativeButton("Cancel",DialogInterface.OnClickListener{
            dialogInterface, i ->
            dialogInterface.cancel()
        })

        dialogMessage.setPositiveButton("Yes",DialogInterface.OnClickListener { dialogInterface, i ->
            myReference.removeValue().addOnCompleteListener { task->
                if(task.isSuccessful)
                {
                    usersAdapter.notifyDataSetChanged()

                    Toast.makeText(applicationContext,"All users were deleted",Toast.LENGTH_SHORT).show()
                }
            }
        })

        dialogMessage.create().show()


    }
}