package com.the.news

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.the.news.databinding.ActivityLoginBinding


class LoginAct : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var progressDialog: ProgressDialog? = null
    var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = ProgressDialog(this)

        progressDialog!!.setTitle("Please wait");
        progressDialog!!.setCanceledOnTouchOutside(false);

        binding.noAccountTv.setOnClickListener {
            var intent = Intent(this, RegisterAct::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener {
            loginUser()
        }

        binding.forgetPasswordTv.setOnClickListener {
            var intent = Intent(this, ForgetPassword::class.java)
            startActivity(intent)
        }
    }

   private fun loginUser(){
      var email = binding.emailEt.text.toString().trim()
       var password = binding.passwordEt.text.toString().trim()


       if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
           Toast.makeText(this, "Invalid email patters...", Toast.LENGTH_SHORT).show();
           return;
       }

       if (TextUtils.isEmpty(password)) {
           Toast.makeText(this, "Enter password...", Toast.LENGTH_SHORT).show();
           return;
       }


       progressDialog!!.setMessage("Loging In...");
       progressDialog!!.show();


       firebaseAuth!!.signInWithEmailAndPassword(email, password)
           .addOnSuccessListener {

               startActivity(Intent(this@LoginAct, MainActivity::class.java))

           }.addOnFailureListener { e ->
               progressDialog!!.dismiss()
               Toast.makeText(this@LoginAct, "" + e.message, Toast.LENGTH_SHORT).show()
           }

   }


}