package com.the.news

import android.Manifest

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Patterns
import android.view.View

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

import com.the.news.databinding.ActivityRegisterBinding

class RegisterAct : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding


    private var image_uri: Uri? = null

    private var progressDialog: ProgressDialog? = null
    var firebaseAuth: FirebaseAuth? = null


    private val CAMERA_REQUEST_CODE = 200
    private val STORAGE_REQUEST_CODE = 300

    private val IMAGE_PICK_GALLERY_CODE = 400
    private val IMAGE_PICK_CAMERA_CODE = 500

    private lateinit var  cameraPermissions: Array<String>
    private lateinit var storagePermissions: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        cameraPermissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        storagePermissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("Please wait")
        progressDialog!!.setCanceledOnTouchOutside(false)

        binding.registerBtn.setOnClickListener(View.OnClickListener {
            inputData()
        })


        binding.profIv.setOnClickListener {

            showImagePickDialog();

        }


    }


    private fun showImagePickDialog() {

        val options = arrayOf("Camera", "Galery")
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Image").setItems(options,
            DialogInterface.OnClickListener { dialog, which ->
                if (which == 0) {
                    if (checkCamerraPermission()) {
                        pickFromCamerra()
                    } else {
                        requestCamerraPermission()
                    }
                } else {
                    if (checkStroragePermission()) {
                        pickFromGalery()
                    } else {
                        requestStoragePermission()
                    }
                }
            }).show()

    }


    private fun pickFromGalery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE)
    }

    private fun pickFromCamerra() {
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp image title")
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp image description")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE)
    }


    private fun checkStroragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE)
    }

    private fun checkCamerraPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        val result1 = ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED
        return result && result1
    }


    private fun requestCamerraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResualt: IntArray,
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                run {
                    if (grantResualt.size > 0) {
                        val cameraAccepted =
                            grantResualt[0] == PackageManager.PERMISSION_GRANTED
                        if (cameraAccepted) {
                            pickFromCamerra()
                        } else {
                            Toast.makeText(this,
                                "Camera permissions are nesesery...",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                run {
                    if (grantResualt.size > 0) {
                        pickFromGalery()
                    }
                }
            }
            STORAGE_REQUEST_CODE -> {
                if (grantResualt.size > 0) {
                    pickFromGalery()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResualt)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                image_uri = data!!.data
                binding.profIv.setImageURI(image_uri)
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                binding.profIv.setImageURI(image_uri)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    lateinit var email: String
    lateinit var password: String
    lateinit var name: String
    lateinit var description_profile: String

    private fun inputData() {
        email = binding.emailEt.text.toString().trim { it <= ' ' }
        password = binding.passwordEt.text.toString().trim { it <= ' ' }
        name = binding.nameEt.text.toString().trim { it <= ' ' }
        description_profile = binding.descrProfEt.text.toString().trim { it <= ' ' }

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Name is empty...", Toast.LENGTH_SHORT).show()

        }

        if (TextUtils.isEmpty(description_profile)){
            Toast.makeText(this, "Description_profileTv is empty...", Toast.LENGTH_SHORT).show()

        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email patters...", Toast.LENGTH_SHORT).show()

        }
        if (password.length < 6) {
            Toast.makeText(this,
                "Password must be at least 6 characters long...",
                Toast.LENGTH_SHORT).show()

        }
        createAccount()
    }


    private fun createAccount() {
        progressDialog!!.setMessage("Creating account...")
        progressDialog!!.show()
        firebaseAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                saveFirebaseData()
            }.addOnFailureListener {
                progressDialog!!.dismiss()
                Toast.makeText(RegisterActivity@this, "Register" , Toast.LENGTH_SHORT).show();
            }
    }


    private fun saveFirebaseData() {
        progressDialog!!.setMessage("saving Account Info...")
        val timesTemp = "" + System.currentTimeMillis()
        if (image_uri == null) {
            val hashMap = HashMap<String, Any>()
            hashMap["uid"] = "" + firebaseAuth!!.uid
            hashMap["email"] = "" + email
            hashMap["password"] = "" + password
            hashMap["name"] = "" + name
            hashMap["description_profile"] = "" + description_profile
            hashMap["timesTemp"] = "" + timesTemp
            hashMap["profileImage"] = ""
            val ref = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth!!.uid!!)


            ref.setValue(hashMap)
                .addOnSuccessListener {
                    progressDialog!!.dismiss()
                    finish()
                }
                .addOnFailureListener {
                    progressDialog!!.dismiss()
                    finish()
                }
        } else {
            val filePathandName = "Profile image/" + "" + firebaseAuth!!.uid
            val storageReference = FirebaseStorage.getInstance().getReference(filePathandName)
            storageReference.putFile(image_uri!!)
                .addOnSuccessListener { taskSnapshot ->
                    val uriTask = taskSnapshot.storage.downloadUrl
                    while (!uriTask.isSuccessful);
                    val downloadImageUri = uriTask.result
                    if (uriTask.isSuccessful) {
                        val hashMap = HashMap<String, Any>()
                        hashMap["uid"] = "" + firebaseAuth!!.uid
                        hashMap["email"] = "" + email
                        hashMap["password"] = "" + password
                        hashMap["description_profile"] = "" + description_profile
                        hashMap.put("name", "" + name);
                        hashMap["timesTemp"] = "" + timesTemp
                        hashMap["profileImage"] = "" + downloadImageUri
                        val ref = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth!!.uid!!)
                        ref.setValue(hashMap)
                            .addOnSuccessListener {
                                progressDialog!!.dismiss()
                                finish()
                            }
                            .addOnFailureListener {
                                progressDialog!!.dismiss()
                                finish()
                            }
                    }
                }.addOnFailureListener {
                    progressDialog!!.dismiss()
                }
        }
    }




}