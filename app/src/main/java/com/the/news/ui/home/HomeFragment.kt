package com.the.news.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import com.the.news.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: ViewModelPublication

    private var firebaseUser: FirebaseUser? = null

    lateinit var adapterPublication: AdapterPublication



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        firebaseUser = FirebaseAuth.getInstance().currentUser

        binding.rvPublication.layoutManager = LinearLayoutManager(context)
        binding.rvPublication.setHasFixedSize(true)
        viewModel = ViewModelProvider(this).get(ViewModelPublication::class.java)


        adapterPublication = AdapterPublication(context)



        viewModel.allPublications.observe(viewLifecycleOwner, Observer {
            adapterPublication.updatePostList(it)
            binding.rvPublication.adapter = adapterPublication

        })


        return root
    }


}


