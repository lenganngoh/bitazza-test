package gohlengann.apps.bitazza_test.data.remote.repository

import com.google.gson.Gson
import gohlengann.apps.bitazza_test.data.local.AuthenticatedUserDao
import gohlengann.apps.bitazza_test.data.model.request.AddUserAPIKeyRequest
import gohlengann.apps.bitazza_test.data.model.request.AuthenticateRequest
import gohlengann.apps.bitazza_test.data.model.request.MessageAction
import gohlengann.apps.bitazza_test.data.model.entity.AuthenticatedUser
import gohlengann.apps.bitazza_test.data.remote.Functions
import gohlengann.apps.bitazza_test.data.remote.WSListener
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val request: Request,
    private val gson: Gson,
    private val authenticatedUserDao: AuthenticatedUserDao
) {

    private lateinit var listener: WSListener

    fun openWebSocket(callback: WSListener.WSCallback) {
        listener = WSListener(callback)
        okHttpClient.newWebSocket(request, listener)
        okHttpClient.dispatcher.executorService.shutdown()
    }

    fun authenticate(webSocket: WebSocket, username: String, password: String) {
        webSocket.send(gson.toJson(constructMessageAction(username, password)))
    }

    private fun constructMessageAction(username: String, password: String): MessageAction {
        return MessageAction(
            0,
            0,
            Functions.AUTHENTICATE_USER.function,
            gson.toJson(AuthenticateRequest(username, password))
        )
    }

    fun getAuthenticatedUser() = authenticatedUserDao.getAuthenticatedUser()

    fun insertAuthenticatedUser(authenticatedUser: AuthenticatedUser) =
        authenticatedUserDao.insertAuthenticatedUser(authenticatedUser)

    fun addUserAPIKey(webSocket: WebSocket, userId: Long) {
        webSocket.send(gson.toJson(constructAddUserAPIKeyAction(userId)))
    }

    private fun constructAddUserAPIKeyAction(userId: Long): MessageAction {
        return MessageAction(
            0,
            0,
            Functions.ADD_USER_API_KEY_PERMISSION.function,
            gson.toJson(AddUserAPIKeyRequest(userId, "fbf32cd8234441a29c6ac6590dbecf40", "Trading, AccountReadOnly"))
        )
    }
}