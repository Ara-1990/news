package com.the.news.ui.home


import android.content.Context

import android.view.*
import android.widget.*
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.the.news.R
import com.the.news.data.dbfirebase.model.UsersModel

import com.the.news.utils.px


class AdapterPublication(
    var context: Context?) : RecyclerView.Adapter<AdapterPublication.ViewHolder>() {


    private var publicationList = mutableListOf<UsersModel>()


    private var firebaseUser: FirebaseUser? = null


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tv_publication = view.findViewById<TextView>(R.id.tv_publication)
        var tv_name = view.findViewById<TextView>(R.id.tv_name)
        var tv_description = view.findViewById<TextView>(R.id.tv_description)
        var iv_picture = view.findViewById<ImageView>(R.id.iv_picture)
        var iv_publication = view.findViewById<ImageView>(R.id.iv_publication)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AdapterPublication.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_publication, parent, false)
        return ViewHolder(view)

    }


    fun updatePostList(publicationList: List<UsersModel>) {
        this.publicationList.addAll(publicationList)

    }

    override fun onBindViewHolder(holder: AdapterPublication.ViewHolder, position: Int) {
        firebaseUser = FirebaseAuth.getInstance().currentUser

        var list: UsersModel = publicationList[position]


        holder.tv_publication.text = list.last_publication
        holder.tv_name.text = list.name
        holder.tv_description.text = list.description_profile
        var picture = R.drawable.ic_news
        Picasso.get().load(list.profileImage).centerCrop(Gravity.TOP).resize(60.px, 60.px)
            .placeholder(picture).into(holder.iv_picture)


        if (list.last_publicationImage != null) {
            holder.iv_publication.visibility = View.VISIBLE
            Picasso.get().load(list.last_publicationImage).into(holder.iv_publication)

        }

        var fAuth = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser!!.uid)

        holder.tv_name.setOnClickListener {
            val uid = list.uid
            val followId = list.followId

            val uid_ = UsersModel(uid, followId)

            if (uid!!.equals(fAuth.key)) {
                it.findNavController().navigate(R.id.navigation_profile)

            } else {
                val bundle = bundleOf("uid" to uid_)
                it.findNavController().navigate(R.id.navigation_userProfile, bundle)

            }
        }

        holder.tv_description.setOnClickListener {

            val uid = list.uid
            val followId = list.followId

            val uid_ = UsersModel(uid, followId)


            if (uid!!.equals(fAuth.key)) {
                it.findNavController().navigate(R.id.navigation_profile)

            } else {

                val bundle = bundleOf("uid" to uid_)
                it.findNavController().navigate(R.id.navigation_userProfile, bundle)
            }


        }


    }


    override fun getItemCount(): Int {
        return publicationList.size
    }


}