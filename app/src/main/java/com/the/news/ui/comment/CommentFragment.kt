package com.the.news.ui.comment


import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.the.news.R
import com.the.news.data.dbfirebase.model.CommentModel
import com.the.news.data.dbfirebase.model.UsersModel
import com.the.news.databinding.FragmentCommentBinding
import com.the.news.utils.px


class CommentFragment : Fragment() {


    private var _binding: FragmentCommentBinding? = null
    private val binding get() = _binding!!

    lateinit private var commentAdapter: CommentAdapter

    private lateinit var usersModel: UsersModel
    private lateinit var viewmodel: ViewModelComment

    private var firebaseUser: FirebaseUser? = null
    var firebaseAuth: FirebaseAuth? = null


    lateinit var uid: String
    lateinit var pubId: String
    var lastMessage = String()
    var last_publication = String()


    var commentTimeMillis = ""
    var hashMapcomment: HashMap<String, Any> = HashMap()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentCommentBinding.inflate(inflater, container, false)
        val root: View = binding.root


        arguments?.let { bundle ->

            usersModel = bundle.getParcelable("comment")!!
        }

        viewmodel = ViewModelProvider(this).get(ViewModelComment::class.java)


        firebaseUser = FirebaseAuth.getInstance().currentUser
        uid = usersModel.uid.toString()
        pubId = usersModel.pubId


        binding.tvName.text = usersModel.name
        binding.tvDescription.text = usersModel.description_profile
        binding.tvPublication.text = usersModel.last_publication


        val icPlaceholderRes = R.drawable.ic_news
        Picasso.get()
            .load(usersModel.profileImage)
            .centerCrop(Gravity.TOP)
            .resize(200.px, 200.px)
            .error(icPlaceholderRes)
            .placeholder(icPlaceholderRes)
            .into(binding.roundImP)



        Picasso.get()
            .load(usersModel.last_publicationImage)
            .centerCrop(Gravity.TOP)
            .resize(200.px, 200.px)
            .error(icPlaceholderRes)
            .placeholder(icPlaceholderRes)
            .into(binding.ivPublication)


        firebaseAuth = FirebaseAuth.getInstance()


        binding.rvComment.layoutManager = LinearLayoutManager(context)
        binding.rvComment.setHasFixedSize(true)

        commentAdapter = CommentAdapter(
            context,
            deleteComment = { it -> viewmodel.deleteComment(it) },
        )

        viewmodel.getComments(pubId)


        viewmodel.allcomments.observe(viewLifecycleOwner, Observer {

            commentAdapter.commentList(it)

            binding.rvComment.adapter = commentAdapter


        })


        binding.sendComment.setOnClickListener {

            var text = binding.enterComm.text.toString()

            if (text.isNotEmpty()) {
                var ref: DatabaseReference =
                    FirebaseDatabase.getInstance().getReference("Users").child("Comments")

                commentTimeMillis = "" + System.currentTimeMillis()

                val publisherRef = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(firebaseAuth!!.uid!!)

                val getComment = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(uid)
                getComment.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild("last_publication")) {
                            var element = snapshot.getValue(UsersModel::class.java)
                            last_publication = element!!.last_publication.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })

                val pubChild: DatabaseReference =
                    FirebaseDatabase.getInstance().getReference().child("Users").child(uid)
                        .child("Publications")
                pubChild.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        lastMessage = snapshot.children.last().key.toString()
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })

                publisherRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val user = snapshot.getValue(UsersModel::class.java)
                            var publisherImage = user!!.profileImage
                            var publisherName = user.name

                            hashMapcomment.put("comment", text)
                            hashMapcomment["publisherImage"] = publisherImage.toString()
                            hashMapcomment["publisherName"] = publisherName.toString()
                            hashMapcomment["userId"] = uid
                            hashMapcomment["pubId"] = pubId

                            ref.child(commentTimeMillis).setValue(hashMapcomment)

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })


            } else {

                Toast.makeText(context, "can not write comment without text", Toast.LENGTH_SHORT).show()
            }
            it.findNavController().navigate(R.id.navigation_home)


        }

        return root

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}