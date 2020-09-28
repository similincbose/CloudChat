package dev.similin.cloudchat.model

import com.google.gson.annotations.SerializedName

class UserResponseApi {

    data class UserResponse(
        @SerializedName("users")
        val users: Map<String, Users>?
    )

    data class Users(

        @SerializedName("userName")
        val userName: String?,

        @SerializedName("about")
        val about: String?,

        @SerializedName("imageUrl")
        val imageUrl: String?
    )

}

