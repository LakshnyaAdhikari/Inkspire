package com.example.inkspire.Model

import android.os.Parcel
import android.os.Parcelable

data class BlogItemModel(
    val heading: String? ="null",
    val userName: String? ="null",
    val date: String? ="null",
    val post: String? ="null",
    var likeCount:Int=0,
    val ProfileImage:String?="null",
    var postId:String?="null",
    var likedBy: MutableList<String> = mutableListOf()
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()?: "null",
        parcel.readString()?: "null",
        parcel.readString()?: "null",
        parcel.readString()?: "null",
        parcel.readInt()

    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(heading)
        parcel.writeString(userName)
        parcel.writeString(date)
        parcel.writeString(post)
        parcel.writeInt(likeCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BlogItemModel> {
        override fun createFromParcel(parcel: Parcel): BlogItemModel {
            return BlogItemModel(parcel)
        }

        override fun newArray(size: Int): Array<BlogItemModel?> {
            return arrayOfNulls(size)
        }
    }

}
