package com.the.news.ui.comment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import com.the.news.R
import com.the.news.data.dbfirebase.model.CommentModel


class CommentAdapter(val context: Context?,
                     private val deleteComment: (String) -> Unit, )
    : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    private var mComment = ArrayList<CommentModel>()
    private var firebaseUser: FirebaseUser? = null

    fun commentList(mComment: List<CommentModel>) {
        this.mComment.addAll(mComment)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var publisherIv = view.findViewById<ImageView>(R.id.publisherIv)
        var user_nameTv = view.findViewById<TextView>(R.id.user_name_comment)
        var commentTv = view.findViewById<TextView>(R.id.comment_comment)
        var closeComment = view.findViewById<ImageView>(R.id.close_comment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_comment, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        firebaseUser = FirebaseAuth.getInstance().currentUser

        val comment = mComment[position]
        holder.commentTv.text = comment.comment
        holder.user_nameTv.text = comment.publisherName


        Picasso.get().load(comment.publisherImage).placeholder(R.drawable.ic_news)
            .into(holder.publisherIv)

        if(firebaseUser!!.uid.equals(comment.userId)){
            holder.closeComment.visibility = View.VISIBLE

        }

        else{
            holder.closeComment.visibility = View.GONE
        }

        holder.closeComment.setOnClickListener {

            deleteComment.invoke(comment.comment!!)

            it.findNavController().navigate(R.id.navigation_home)
        }
    }

    override fun getItemCount(): Int {
        return mComment.size
    }
}