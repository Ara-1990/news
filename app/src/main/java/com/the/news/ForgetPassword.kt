package com.the.news

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.the.news.databinding.ActivityForgetPasswordBinding


class ForgetPassword : AppCompatActivity() {

    private lateinit var binding: ActivityForgetPasswordBinding


    private var progressDialog: ProgressDialog? = null
    var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)




        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = ProgressDialog(this);
        progressDialog!!.setTitle("Please wait");
        progressDialog!!.setCanceledOnTouchOutside(false);




        binding.recoverBtn.setOnClickListener {
            if (binding.emailIv.text.equals("")) {
                Toast.makeText(this, "fill email field", Toast.LENGTH_SHORT).show()

            }
            else{
                recoverPaaword();
            }
        }

    }


    private var email: String? = null

    private fun recoverPaaword() {

        email = binding.emailIv.getText().toString().trim()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email...", Toast.LENGTH_SHORT).show()
            return
        }
        progressDialog!!.setMessage("Sending instruction to reset password....")
        progressDialog!!.show()
        firebaseAuth!!.sendPasswordResetEmail(email!!)
            .addOnSuccessListener {
                progressDialog!!.dismiss()
                Toast.makeText(this@ForgetPassword,
                    "Password reset instuctions sent to your email",
                    Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { e ->
                progressDialog!!.dismiss()
                Toast.makeText(this@ForgetPassword, "" + e.message, Toast.LENGTH_SHORT)
                    .show()
            }
    }


}