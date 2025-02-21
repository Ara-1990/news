package com.the.news.ui.profile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.the.news.R
import com.the.news.databinding.FragmentProfileBinding
import com.the.news.ui.following.AdapterFollowing


class Profile : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewmodel: ViewModelProfile

    lateinit var adapterProfile: AdapterFollowing

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rv.layoutManager = LinearLayoutManager(context)
        binding.rv.setHasFixedSize(true)
        viewmodel = ViewModelProvider(this).get(ViewModelProfile::class.java)


        adapterProfile = AdapterFollowing(context,
            deletePost = {it->viewmodel.delletePost(it)}
        )


        viewmodel.myPublications.observe(viewLifecycleOwner, Observer {
            adapterProfile.updatePostList(it)
            binding.rv.adapter = adapterProfile

        })

        binding.makePost.setOnClickListener {

            findNavController().navigate(R.id.navigation_makePost)
        }

        return root
    }

}