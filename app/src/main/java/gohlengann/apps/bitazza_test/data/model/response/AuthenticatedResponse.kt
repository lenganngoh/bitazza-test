package gohlengann.apps.bitazza_test.data.model.response

import com.google.gson.annotations.SerializedName
import gohlengann.apps.bitazza_test.data.model.entity.User

data class AuthenticatedResponse(
    @SerializedName("Authenticated") val authenticated: Boolean,
    @SerializedName("SessionToken") val session_token: String,
    @SerializedName("User") val user: User
)