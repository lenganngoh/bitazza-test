package gohlengann.apps.bitazza_test.data.model.entity

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("UserId") val id: Long = 0,
    @SerializedName("UserName") val username: String? = "",
    @SerializedName("Email") val email: String? = "",
    @SerializedName("EmailVerified") val is_verified: Boolean = false,
    @SerializedName("AccountId") val account_id: Long = 0,
    @SerializedName("OMSId") val oms_id: Long = 0
)