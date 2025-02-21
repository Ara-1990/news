package com.the.news.ui.post

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.the.news.databinding.FragmentMakePostBinding

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.HashMap


class MakePost : Fragment() {


    private var _binding: FragmentMakePostBinding? = null


    lateinit var firebaseAuth: FirebaseAuth


    private val binding get() = _binding!!

    private var image_uri: Uri? = null

    private val IMAGE_PICK_CAMERA_CODE = 500
    private val IMAGE_PICK_GALLERY_CODE = 400
    private val CAMERA_REQUEST_CODE = 200
    private val STORAGE_REQUEST_CODE = 300

    private lateinit var cameraPermissions: Array<String>
    private lateinit var storagePermissions: Array<String>

    var curent_publication = mutableListOf<String>()

    var name = String()
    var description_profile = String()
    var uid = String()
    var curent_publicationTimeMillis = ""
    var hashMapCurentPublicat: HashMap<String, Any> = HashMap()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentMakePostBinding.inflate(inflater, container, false)
        val root: View = binding.root

        cameraPermissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        storagePermissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        firebaseAuth = FirebaseAuth.getInstance()

        var _ivPublication = binding.ivPublication

        binding.ibPublication.setOnClickListener {
            _ivPublication.visibility = View.VISIBLE
            showImagePickDialog()
        }


        binding.placeButton.setOnClickListener {
            var text = binding.EdWritePost.text.toString()

            if (text.isNotEmpty() && binding.ivPublication.drawable != null) {

                var ref: DatabaseReference =
                        FirebaseDatabase.getInstance().getReference("Users")
                            .child(firebaseAuth.uid!!).child("Publications")

                curent_publicationTimeMillis = "" + System.currentTimeMillis()

                hashMapCurentPublicat.put("curent_publication", text)
                curent_publication.add(text)


                ref.child(curent_publicationTimeMillis).setValue(hashMapCurentPublicat)

                lastPublication()

                lifecycleScope.launch {
                        curentPublication()
                }

                curent_and_ImagePublication()

                binding.EdWritePost.text.clear()
                binding.ivPublication.setImageDrawable(null)

            } else {
                Toast.makeText(context, "Text and image field cant be empty", Toast.LENGTH_LONG).show()

            }

        }




        return root


    }







    private fun lastPublication() {
        var text = binding.EdWritePost.text.toString()
        if (text.isNotEmpty()) {

            var _ref: DatabaseReference =
                FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.uid!!)
            var _hashMap: HashMap<String, Any> = HashMap()


            _hashMap.put("last_publication", text)

            _ref.updateChildren(_hashMap)



        }
    }

    private fun curent_and_ImagePublication() {


        val filePathandName = "Publication image/" + "" + firebaseAuth.uid
        val storageReference = FirebaseStorage.getInstance().getReference(filePathandName)
        storageReference.putFile(image_uri!!)
            .addOnSuccessListener { taskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val downloadImageUri = uriTask.result
                if (uriTask.isSuccessful) {

                    hashMapCurentPublicat["curent_publicationImage"] = "" + downloadImageUri

                    val ref = FirebaseDatabase.getInstance().getReference("Users")
                        .child(firebaseAuth.uid!!).child("Publications")
                    ref.child(curent_publicationTimeMillis).setValue(hashMapCurentPublicat)
                        .addOnSuccessListener {

                        }
                        .addOnFailureListener {

                        }
                }

            }.addOnFailureListener {

            }


        lastPublicationIv()

    }


    private fun lastPublicationIv() {
        if (binding.ivPublication.isVisible) {
            val filePathandName = "Publication image/" + "" + firebaseAuth.uid
            val storageReference = FirebaseStorage.getInstance().getReference(filePathandName)
            storageReference.putFile(image_uri!!)
                .addOnSuccessListener { taskSnapshot ->
                    val uriTask = taskSnapshot.storage.downloadUrl
                    while (!uriTask.isSuccessful);
                    val downloadImageUri = uriTask.result
                    if (uriTask.isSuccessful) {
                        val hashMap = HashMap<String, Any>()
                        hashMap["last_publicationImage"] = "" + downloadImageUri

                        var _ref: DatabaseReference =
                            FirebaseDatabase.getInstance().getReference("Users")
                                .child(firebaseAuth.uid!!)
                        _ref.updateChildren(hashMap)

                    }

                }.addOnFailureListener {


                }


        }

    }

    suspend fun curentPublication() = coroutineScope {


        launch {
            delay(1000)

            var ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseAuth.uid!!).child("Publications")

            hashMapCurentPublicat.put("name", name)
            hashMapCurentPublicat.put("description_profile", description_profile)
            hashMapCurentPublicat.put("pubId", curent_publicationTimeMillis) //նոր
            hashMapCurentPublicat.put("uid", uid) //նոր
            ref.child(curent_publicationTimeMillis).setValue(hashMapCurentPublicat)

        }

        var ref2: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.uid!!)
        ref2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                name = snapshot.child("name").value.toString()
                description_profile = snapshot.child("description_profile").value.toString()
                uid = snapshot.child("uid").value.toString()

            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        val storageReference: StorageReference = FirebaseStorage.getInstance().getReference()
        var strorageRef: StorageReference =
            storageReference.child("Profile image/" + "" + firebaseAuth.uid!!)
        strorageRef.downloadUrl
            .addOnSuccessListener {
                hashMapCurentPublicat["profileImage"] = "" + it

            }

    }

    private fun showImagePickDialog() {

        val options = arrayOf("Camera", "Galery")
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
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

    private fun checkCamerraPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(requireContext(),
            Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        val result1 = ContextCompat.checkSelfPermission(requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED
        return result && result1
    }

    private fun pickFromCamerra() {
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp image title")
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp image description")
        image_uri = context?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE)
    }

    private fun requestCamerraPermission() {
        requestPermissions(cameraPermissions, CAMERA_REQUEST_CODE)

    }


    private fun checkStroragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun pickFromGalery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE)
    }

    private fun requestStoragePermission() {
        requestPermissions(storagePermissions, STORAGE_REQUEST_CODE)


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResualt: IntArray,
        ){
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                run {
                    if (grantResualt.size > 0) {
                        val cameraAccepted =
                            grantResualt[0] == PackageManager.PERMISSION_GRANTED
                        if (cameraAccepted) {
                            pickFromCamerra()
                        } else {
                            Toast.makeText(context,
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
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                image_uri = data!!.data
                binding.ivPublication.setImageURI(image_uri)
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                binding.ivPublication.setImageURI(image_uri)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


}