package gohlengann.apps.bitazza_test.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gohlengann.apps.bitazza_test.data.model.entity.AuthenticatedUser

@Dao
interface AuthenticatedUserDao {
    @Query("SELECT * FROM authenticated_user LIMIT 1")
    fun getAuthenticatedUser() : LiveData<AuthenticatedUser?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAuthenticatedUser(authenticatedUser: AuthenticatedUser)

    @Query("DELETE FROM authenticated_user")
    fun clearAuthenticatedUser()
}