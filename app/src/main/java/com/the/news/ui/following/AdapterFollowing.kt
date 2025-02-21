package com.the.news.ui.following

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.*
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.the.news.R

import com.the.news.data.dbfirebase.model.UsersModel

import com.the.news.utils.px


class AdapterFollowing(
    var context: Context?,
    private val deletePost: (String) -> Unit,
) :
    RecyclerView.Adapter<AdapterFollowing.ViewHolder>() {


    private var firebaseUser: FirebaseUser? = null


    fun updatePostList(publicationList: List<UsersModel>) {
        differ.submitList(publicationList)

    }


    private val differConfig = object : DiffUtil.ItemCallback<UsersModel>() {

        override fun areItemsTheSame(oldItem: UsersModel, newItem: UsersModel): Boolean {
            return oldItem.uid == newItem.uid
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: UsersModel, newItem: UsersModel): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, differConfig)


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tv_publication = view.findViewById<TextView>(R.id.tv_publication)
        var tv_name = view.findViewById<TextView>(R.id.tv_name)
        var tv_description = view.findViewById<TextView>(R.id.tv_description)
        var iv_picture = view.findViewById<ImageView>(R.id.iv_picture)
        var iv_publication = view.findViewById<ImageView>(R.id.iv_publication)

        var like = view.findViewById<ImageView>(R.id.like)
        var likes_count = view.findViewById<TextView>(R.id.likes_count)
        var comment = view.findViewById<ImageView>(R.id.comment)
        var ic_postDelete = view.findViewById<ImageView>(R.id.ic_postDelete)


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_following, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        firebaseUser = FirebaseAuth.getInstance().currentUser

        val list = differ.currentList[position]

        isLikes(list.pubId, holder.like)
        numberOfLikes(holder.likes_count, list.pubId)


        holder.like.setOnClickListener {
            if (holder.like.tag == "Like") {
                FirebaseDatabase.getInstance().getReference()
                    .child("Users").child("Likes")
                    .child(list.pubId)
                    .child(firebaseUser!!.uid)
                    .setValue(true)
            } else {
                FirebaseDatabase.getInstance().reference
                    .child("Users").child("Likes")
                    .child(list.pubId)
                    .child(firebaseUser!!.uid)
                    .removeValue()


            }
            it.findNavController().navigate(R.id.navigation_home)
        }

        holder.tv_publication.text = list.curent_publication
        holder.tv_name.text = list.name
        holder.tv_description.text = list.description_profile
        var picture = R.drawable.ic_news
        Picasso.get().load(list.profileImage).centerCrop(Gravity.TOP).resize(60.px, 60.px)
            .placeholder(picture).into(holder.iv_picture)
        holder.iv_publication.visibility = View.VISIBLE
        Picasso.get().load(list.curent_publicationImage).into(holder.iv_publication)


        holder.tv_name.setOnClickListener {

            val uid = list.uid
            val uid_ = UsersModel(uid)
            val bundle = bundleOf("uid" to uid_)
            it.findNavController().navigate(R.id.navigation_userProfile, bundle)

        }

        holder.tv_description.setOnClickListener {


            val uid = list.uid
            val uid_ = UsersModel(uid)

            val bundle = bundleOf("uid" to uid_)
            it.findNavController().navigate(R.id.navigation_userProfile, bundle)


        }


        holder.comment.setOnClickListener {

            val name = list.name
            val description_profile = list.description_profile
            val curent_publication = list.curent_publication
            val profileImage = list.profileImage
            val curent_publicationImage = list.curent_publicationImage
            val commentUid = list.uid
            val commentPid = list.pubId

            try {
                if (name!!.isNotEmpty() && description_profile!!.isNotEmpty()) {

                    val comment = UsersModel(
                        commentUid,
                        null,
                        curent_publication,
                        curent_publicationImage,
                        name,
                        description_profile,
                        profileImage,
                        commentPid,

                        )
                    val bundle = bundleOf("comment" to comment)


                    it.findNavController().navigate(R.id.navigation_comment, bundle)

                } else {
                    Toast.makeText(context, "empty", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {

            }


        }

        if (list.uid.equals(firebaseUser!!.uid)) {
            holder.ic_postDelete.visibility = View.VISIBLE
        } else {
            holder.ic_postDelete.visibility = View.GONE

        }
        holder.ic_postDelete.setOnClickListener {


            val dialog = Dialog(context!!)

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.alert_dialog)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            var positvie = dialog.findViewById<Button>(R.id.positiv_tv)
            var negative = dialog.findViewById<Button>(R.id.negativ_tv)

            positvie.setOnClickListener {

                deletePost.invoke(list.curent_publication!!)

                dialog.dismiss()
            }
            negative.setOnClickListener {
                dialog.dismiss()

            }
            dialog.show()


        }


    }

    private fun isLikes(pubId: String, like: ImageView?) {


        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val likesRef =
            FirebaseDatabase.getInstance().reference.child("Users").child("Likes").child(pubId)

        likesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(firebaseUser!!.uid).exists()) {
                    like!!.setImageResource(R.drawable.ic_favorite_24)
                    like.tag = "Liked"
                } else {
                    like!!.setImageResource(R.drawable.ic_favourite_inactive)
                    like.tag = "Like"
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun numberOfLikes(likes_count: TextView?, pubId: String) {
        val likesRef = FirebaseDatabase.getInstance().reference.child("Users").child("Likes").child(pubId)


        likesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    likes_count!!.text = snapshot.childrenCount.toString() + "likes"
                } else {
                    likes_count!!.text = ""

                }

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })


    }



    override fun getItemCount(): Int {

        return differ.currentList.size
    }


}