package com.example.secrett

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignup: Button
    private  lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()

        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignup = findViewById(R.id.btnSignup)

        btnSignup.setOnClickListener {
            val intent = Intent(this@LoginActivity ,SignupActivity::class.java)
            startActivity(intent)
            finish()
        }


        btnLogin.setOnClickListener {


            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()){
                login(email,password)

            }else{
                Toast.makeText(this@LoginActivity, "Please fill all the fields",
                    Toast.LENGTH_SHORT).show()

            }

        }
    }
private fun login(email: String,password : String){
    mAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information

                val intent = Intent(this@LoginActivity,MainActivity::class.java)
                startActivity(intent)

            } else {
                // If sign in fails, display a message to the user.

                Toast.makeText(this@LoginActivity, "Wrong email or password entered or User does not exist ",
                    Toast.LENGTH_SHORT).show()

            }
        }
}

}