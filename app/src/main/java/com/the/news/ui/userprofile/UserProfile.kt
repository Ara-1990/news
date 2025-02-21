package com.the.news.ui.userprofile


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.the.news.R
import com.the.news.data.dbfirebase.model.UsersModel
import com.the.news.databinding.FragmentUserProfileBinding
import com.the.news.ui.following.AdapterFollowing



class UserProfile : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null

    private val binding get() = _binding!!

    lateinit var firebaseAuth: FirebaseAuth


    lateinit var adapterUserProfile: AdapterFollowing
    private lateinit var viewModel: UserProfViewModel

    lateinit var uid: String
    lateinit var followId: String
    var name = String()

    private lateinit var usersModel: UsersModel
    var mudel = mutableListOf<UsersModel>()



    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = ViewModelProvider(this).get(UserProfViewModel::class.java)

        binding.rv.layoutManager = LinearLayoutManager(context)
        binding.rv.setHasFixedSize(true)
        adapterUserProfile = AdapterFollowing(context,
            deletePost = { it -> "" }
        )

        viewModel.myPublications.observe(viewLifecycleOwner, Observer {
            it.forEach {

                if (uid == it.uid) {

                    mudel.add(it)


                } else {
                    binding.rv.adapter = null

                }
            }

            adapterUserProfile.updatePostList(mudel)
            adapterUserProfile.notifyDataSetChanged()
            binding.rv.adapter = adapterUserProfile

        })




        firebaseAuth = FirebaseAuth.getInstance()

        arguments.let { bundle ->
            usersModel = bundle!!.getParcelable("uid")!!
        }

        uid = usersModel.uid.toString()
        followId = usersModel.followId.toString()


        binding.follow.setOnClickListener {

            viewModel.sendFollow(uid)
            it.findNavController().navigate(R.id.navigation_home)

        }

        binding.unfollow.setOnClickListener {
            viewModel.delleteFollow(uid)
            it.findNavController().navigate(R.id.navigation_home)

        }

        checkFollow()



        return root

    }


    fun checkFollow() {
        var followauth =
            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.uid!!)
                .child("Follow")
        followauth.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    if (snapshot.exists()) {

                        followauth.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (ds in snapshot.children) {
                                    if (ds.child("followTo").value!!.equals(uid)) {
                                        binding.follow.visibility = View.GONE
                                        binding.unfollow.visibility = View.VISIBLE
                                        break

                                    } else {
                                        binding.follow.visibility = View.VISIBLE
                                        binding.unfollow.visibility = View.GONE


                                    }
                                }


                            }

                            override fun onCancelled(error: DatabaseError) {
                            }

                        })
                    } else {
                        binding.follow.visibility = View.VISIBLE
                        binding.unfollow.visibility = View.GONE

                    }


                } catch (exeption: Exception) {

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }


}