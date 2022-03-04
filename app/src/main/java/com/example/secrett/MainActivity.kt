package com.example.secrett

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.secrett.R.menu.menu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private  lateinit var userRecyclerview: RecyclerView
    private lateinit var userList:ArrayList<User>
    private  lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
         mDbRef=FirebaseDatabase.getInstance().getReference()
        userList = ArrayList()
        adapter = UserAdapter(this,userList)

        userRecyclerview =findViewById(R.id.userRecyclerView)

        userRecyclerview.layoutManager=LinearLayoutManager(this)
        userRecyclerview.adapter=adapter

        mDbRef.child("user").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
             for (postSnapShot in snapshot.children){
                 val currentUser = postSnapShot.getValue(User::class.java)
                 if (mAuth.currentUser?.uid!=currentUser?.uid) {
                     userList.add(currentUser!!)
                 }
             }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

       if (item.itemId==R.id.logout){
       mAuth.signOut()
           val intent = Intent(this@MainActivity,LoginActivity::class.java)
           finish()
           startActivity(intent)
           return true
       }

        return true
    }
}