package com.the.news


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.the.news.databinding.ActivityMainBinding
import com.the.news.ui.BaseActivity
import com.the.news.utils.visibleOrGone


class MainActivity : BaseActivity() {


    private lateinit var binding: ActivityMainBinding


    var firebaseAuth: FirebaseAuth? = null


    var dialog: AlertDialog.Builder? = null


    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance();


        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        setSupportActionBar(binding.appBarMain.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navViewDrawer: NavigationView = binding.navViewDrawer

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_follow,
                R.id.navigation_news,
                R.id.navigation_search,
                R.id.navigation_favourite,
                R.id.navigation_details,
                R.id.navigation_comment
            ),
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        navViewDrawer.setupWithNavController(navController)
        NavigationUI.setupWithNavController(binding.appBarMain.navView, navController)


        binding.appBarMain.navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            handleBottomNavViewVisibility(destination.id)
        }


        navViewDrawer.menu.findItem(R.id.navigation_logout).setOnMenuItemClickListener({ menuItem ->
            logout()
            true
        })

        var user = firebaseAuth!!.currentUser

        navViewDrawer.menu.findItem(R.id.delete_account).setOnMenuItemClickListener({ menuItem ->

            dialog = AlertDialog.Builder(this)
            dialog!!.setTitle("Delete account")
                .setCancelable(true)
                .setPositiveButton("Delete account"
                ) { dialog, which ->
                    if(user != null) {
                        deleteCurrentUser()
                    }
                    else{
                        Toast.makeText(this, "please login ", Toast.LENGTH_SHORT).show()
                    }
                }.show()
            true
        })




        if (user == null) {
            navViewDrawer.menu.findItem(R.id.navigation_profile)
                .setOnMenuItemClickListener({ menuItem ->
                    startActivity(Intent(this, LoginAct::class.java))
                    finish()
                    true
                })
        }


    }

    private fun deleteCurrentUser(){

        FirebaseDatabase.getInstance().reference.child("Users")
            .child(FirebaseAuth.getInstance().currentUser!!
                .uid)
            .setValue(null)
            .addOnSuccessListener {
                FirebaseAuth.getInstance().currentUser!!.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this@MainActivity, LoginAct::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@MainActivity,
                                "Deleate account failed",
                                Toast.LENGTH_SHORT)
                        }
                    }
            }

    }

    private fun handleBottomNavViewVisibility(id: Int) {
        binding.appBarMain.navView.visibleOrGone(id != R.id.navigation_details
                && id != R.id.navigation_comment
                && id != R.id.navigation_profile && id != R.id.navigation_makePost && id != R.id.navigation_userProfile
                && id != R.id.navigation_follow
        )
        actionBar?.apply {
            if (id != R.id.navigation_details) show() else hide()
            if (id != R.id.navigation_comment) show() else hide()
            if (id != R.id.navigation_profile) show() else hide()
            if (id != R.id.navigation_makePost) show() else hide()
            if (id != R.id.navigation_userProfile) show() else hide()
            if (id != R.id.navigation_follow) show() else hide()


        }

    }


    private fun logout() {
        firebaseAuth!!.signOut();
        var intent = Intent(applicationContext, LoginAct::class.java)
        startActivity(intent)
        checkUser();
    }


    private fun checkUser() {
        var user = firebaseAuth!!.currentUser

        if (user == null) {
            startActivity(Intent(this, LoginAct::class.java))
            finish()
        }


    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}