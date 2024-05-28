package com.example.stepup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.stepup.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class signupActivity : AppCompatActivity() {
    private val binding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(
            layoutInflater
        ) }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //initialize auth
        auth = FirebaseAuth.getInstance()

        binding.SignInButton.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.RegisterButton.setOnClickListener{
            val email: String = binding.email.text.toString()
            val userName = binding.username.text.toString()
            val password = binding.Password.text.toString()
            val repeatPassword = binding.repeatPassword.text.toString()

            //check if any field is blank
            if(email.isEmpty() || userName.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()){
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
            }
            else if(repeatPassword != password){
                Toast.makeText(this, "repeat password doesn't match", Toast.LENGTH_SHORT).show()
            }
            else{
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this){task ->if(task.isSuccessful){
                        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                    else{
                        Toast.makeText(this, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }}
            }
        }
    }
}