package com.example.inkspire.Model

import android.provider.ContactsContract.CommonDataKinds.Email

data class UserData(
    val name: String="",
    val email: String="",
    val profileImage:String="",
    val likecount:Int,
    val imageUrl:String
){
    constructor() : this(
        name = "",
        email = "",
        profileImage = "",
        likecount = 0,
        imageUrl = ""
    )

}
