package gohlengann.apps.bitazza_test.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "authenticated_user")
data class AuthenticatedUser(
    @PrimaryKey val id: Long = 0,
    val session_token: String? = null,
    val username: String? = null,
    val email: String? = null,
    val account_id: Long = 0,
    val oms_id: Long = 0
)