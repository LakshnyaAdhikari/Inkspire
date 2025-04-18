package com.example.inkspire.Model

import android.provider.ContactsContract.CommonDataKinds.Email

data class UserData(
    val name: String="",
    val email: String="",
    val profileImage:String="",
    val likeCount:Int=0,
    val imageUrl:String
){
    constructor() : this(
        name = "",
        email = "",
        profileImage = "",
        likeCount = 0,
        imageUrl = ""
    )

}
