package com.the.news.data.repository

import android.content.Context

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import com.the.news.data.dbfirebase.model.CommentModel
import com.the.news.data.dbfirebase.model.UsersModel


class RepositoryPublication {

    var firebaseAuth = FirebaseAuth.getInstance()
    var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

    var model: UsersModel? = null

    var modelPublication: MutableList<UsersModel> = mutableListOf()


    var modelComment: MutableList<CommentModel> = mutableListOf()


    lateinit var context: Context

    private var INCTANCE: RepositoryPublication? = null

    fun getInctance(): RepositoryPublication {
        return INCTANCE ?: synchronized(this) {
            val instance = RepositoryPublication()
            INCTANCE = instance
            instance
        }
    }


    fun getPosts(publicationList: MutableLiveData<List<UsersModel>>) {


        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    var modelPublication: List<UsersModel> = snapshot.children.map { ds ->
                        ds.getValue(UsersModel::class.java)!!

                    }
                    publicationList.postValue(modelPublication)

                } catch (e: Exception) {

                }

            }

            override fun onCancelled(error: DatabaseError) {}

        })

    }




    fun getFollowed(publicationList: MutableLiveData<List<UsersModel>>) {

        var user = firebaseAuth.currentUser

        if (user != null){

        var refFollowed: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.uid!!)
                .child("Follow")
        refFollowed.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    if (snapshot.exists()) {
                        for (ds in snapshot.children) {
                            var uid = ds.child("followTo").value.toString()
                            var ref: DatabaseReference = FirebaseDatabase.getInstance()
                                .getReference("Users")
                                .child(uid)
                            ref.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.hasChild("Publications")) {

                                        var reference: DatabaseReference =
                                            FirebaseDatabase.getInstance()
                                                .getReference("Users")
                                                .child(uid).child("Publications")

                                        reference.addValueEventListener(object :
                                            ValueEventListener {
                                            override fun onDataChange(snapshot4: DataSnapshot) {
                                                try {
                                                    for (ds4 in snapshot4.children) {
                                                        model = ds4.getValue(
                                                            UsersModel::class.java)
                                                        modelPublication.add(model!!)
                                                    }
                                                    publicationList.postValue(modelPublication)

                                                } catch (e: Exception) {

                                                }

                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                            }

                                        })


                                    } else {

                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                }

                            })
                        }
                    } else {
                        FirebaseDatabase.getInstance().getReference("Users")
                            .child(firebaseAuth.uid!!)


                    }


                } catch (exeption: Exception) {

                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })

    }
    }

    fun getMyPublications(publicationList: MutableLiveData<List<UsersModel>>) {

                        var reference: DatabaseReference =
                            FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.uid!!)
                        reference.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.hasChild("Publications")) {
                                    var ref: DatabaseReference =
                                        FirebaseDatabase.getInstance().getReference("Users")
                                            .child(firebaseAuth.uid!!).child("Publications")
                                    ref.addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            try {
                                                var modelPublication: List<UsersModel> =
                                                    snapshot.children.map { ds ->
                                                        ds.getValue(UsersModel::class.java)!!
                                                    }
                                                publicationList.postValue(modelPublication)

                                            } catch (e: Exception) {

                                            }

                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                        }

                                    })


                                }



                            }
                            override fun onCancelled(error: DatabaseError) {
                            }

                        })
    }





    fun getComments(publicationList: MutableLiveData<List<CommentModel>>, pubId: String) {

        var reference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child("Comments")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    for (ds in snapshot.children) {
                        if (ds.child("pubId").value == pubId) {
                            var modelComm = ds.getValue(CommentModel::class.java)
                            modelComment.add(modelComm!!)

                        }

                    }
                    publicationList.postValue(modelComment)

                } catch (e: Exception) {

                }

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }



    fun userProfile(publicationList: MutableLiveData<List<UsersModel>>) {
        var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (uid in snapshot.children) {
                    if (uid.hasChild("Publications")) {
                        var reference2: DatabaseReference =
                            FirebaseDatabase.getInstance().getReference("Users").child(uid.key!!)
                                .child("Publications")
                        reference2.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                try {
                                    for (ds in snapshot.children) {
                                        var model = ds.getValue(UsersModel::class.java)

                                        modelPublication.add(model!!)

                                    }
                                    publicationList.postValue(modelPublication)


                                } catch (e: Exception) {

                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }

                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }


    fun sendFollow(uid: String) {


        var timeMillis = "" + System.currentTimeMillis()

        var hashMap_follow: HashMap<String, Any> = HashMap()
        hashMap_follow.put("followTo", uid)


        var ref: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.uid!!)
                .child("Follow")

        ref.child(timeMillis).setValue(hashMap_follow)


    }


    fun delleteFollow(id: String) {

        var ref_follow =
            FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseAuth.uid!!).child("Follow")

        ref_follow.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    if (ds.child("followTo").value!!.equals(id)) {
                        ds.ref.removeValue()
                    }


                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    fun delletePost(curent_publication: String) {
        var publication = FirebaseDatabase.getInstance().getReference("Users")
            .child(firebaseAuth.uid!!).child("Publications")
        publication.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    if (ds.child("curent_publication").value!!.equals(curent_publication)) {
                        ds.ref.removeValue()

                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    fun deleteComments(comment: String) {
        var refComment = FirebaseDatabase.getInstance().getReference("Users").child("Comments")
        refComment.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    if (ds.child("comment").value!!.equals(comment)) {
                        ds.ref.removeValue()

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}