package com.the.news.data.dbfirebase.model

import android.os.Parcelable
import android.widget.ImageView
import kotlinx.parcelize.Parcelize

@Parcelize
 data class UsersModel(

    var uid:String? = null,
    var followId:String? = null,
     var last_publication:String? = null,
     var last_publicationImage:String? = null,
     var name:String? = null,
     var description_profile: String? = null,
     var profileImage:String? = null,
     var pubId:String = "" ,
     var curent_publication:String? = null,
     var curent_publicationImage:String? = null,
     var comment:String? = null,
     var followTo:String? = null,


): Parcelable



