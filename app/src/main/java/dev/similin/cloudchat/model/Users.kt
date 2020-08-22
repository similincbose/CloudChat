package dev.similin.cloudchat.model

import com.google.gson.annotations.SerializedName

data class Users(


    @SerializedName("user_name")
    val userName: String?,

    @SerializedName("phone_number")
    val phoneNumber: String?,

    @SerializedName("about")
    val about: String?,

    @SerializedName("image_url")
    val imageUrl: String?
)