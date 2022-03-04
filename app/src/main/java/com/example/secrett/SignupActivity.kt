package com.example.secrett

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.secrett.crypter.*


class SignupActivity : AppCompatActivity() {
    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignup: Button
    private  lateinit var mAuth: FirebaseAuth
    private lateinit var mdbRef : DatabaseReference
    lateinit var name: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()


        edtName = findViewById(R.id.edt_name)
        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)

        btnSignup = findViewById(R.id.btnSignup)



        btnSignup.setOnClickListener {
             name = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()){

                signup(name,email,password)

            }else{
                Toast.makeText(this@SignupActivity, "Please fill all the fields",
                    Toast.LENGTH_SHORT).show()

            }


        }
    }

    private fun signup(name:String ,email: String,password: String){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                      //todo public key here while creating user
                      //    val createPublic = PublicKeyCreator()
                   // val publicKey // : String = createPublic.createPublic()
                     addUserToDatabase(name,email, mAuth.currentUser?.uid!!)//,publicKey)
                    val intent = Intent(this@SignupActivity,MainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@SignupActivity,"Some error has occurred",Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(
        name: String,
        email: String,
        uid:String,
       // publicKey: String
    ){
  mdbRef =FirebaseDatabase.getInstance().reference
        mdbRef.child("user").child(uid).setValue(User(name,email,uid))//,publicKey))
    }
}