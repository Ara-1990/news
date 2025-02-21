package com.the.news.ui.following

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.LinearLayoutManager
import com.the.news.databinding.FragmentFollowingBinding


class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewmodel: ViewModelFollow

    lateinit var adapterFollowing: AdapterFollowing


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rvFollowing.layoutManager = LinearLayoutManager(context)
        binding.rvFollowing.setHasFixedSize(true)
        viewmodel = ViewModelProvider(this).get(ViewModelFollow::class.java)
        adapterFollowing = AdapterFollowing(context,
            deletePost = {it->viewmodel.deletePost(it)},

        )

        viewmodel.allPublications.observe(viewLifecycleOwner, Observer {

            adapterFollowing.updatePostList(it)
            binding.rvFollowing.adapter = adapterFollowing

        })

        return root
    }



}