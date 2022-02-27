package gohlengann.apps.bitazza_test.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import gohlengann.apps.bitazza_test.data.model.response.AuthenticatedResponse
import gohlengann.apps.bitazza_test.data.model.request.MessageAction
import gohlengann.apps.bitazza_test.data.model.entity.AuthenticatedUser
import gohlengann.apps.bitazza_test.data.remote.Functions
import gohlengann.apps.bitazza_test.data.remote.WSListener
import gohlengann.apps.bitazza_test.data.remote.repository.LoginRepository
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okio.ByteString

class LoginViewModel @ViewModelInject constructor(
    private val repo: LoginRepository,
    private val gson: Gson
) : ViewModel() {

    private lateinit var webSocket: WebSocket

    val isWebSocketOpen: MutableLiveData<Boolean> = MutableLiveData(false)
    val authentication: MutableLiveData<AuthenticatedUser?> = MutableLiveData(null)

    private val callback: WSListener.WSCallback = object : WSListener.WSCallback {
        override fun connectionOpened(webSocket: WebSocket) {
            this@LoginViewModel.webSocket = webSocket
            viewModelScope.launch {
                isWebSocketOpen.value = true
            }
        }

        override fun onServerResponse(webSocket: WebSocket, text: String) {
            this@LoginViewModel.webSocket = webSocket
            val rawResponse = gson.fromJson(text, MessageAction::class.java)
            when (rawResponse.n) {
                Functions.AUTHENTICATE_USER.function -> {
                    val response = gson.fromJson(rawResponse.o, AuthenticatedResponse::class.java)
                    insertAuthenticatedUser(
                        AuthenticatedUser(
                            response.user.id,
                            response.session_token,
                            response.user.username,
                            response.user.email,
                            response.user.account_id,
                            response.user.oms_id
                        )
                    )
                }
            }
        }

        override fun onServerResponse(webSocket: WebSocket, text: ByteString) {
            this@LoginViewModel.webSocket = webSocket
            TODO("Not yet implemented")
        }

        override fun connectionFailed(webSocket: WebSocket, t: Throwable, response: Response?) {
            this@LoginViewModel.webSocket = webSocket
            TODO("Not yet implemented")
        }
    }

    fun openWebSocket() = repo.openWebSocket(callback)

    fun closeWebSocket() = webSocket.close(1000, null)

    fun authenticate(username: String, password: String) {
        repo.authenticate(webSocket, username, password)
    }

    fun getAuthenticatedUser() = repo.getAuthenticatedUser()

    fun insertAuthenticatedUser(authenticatedUser: AuthenticatedUser) =
        repo.insertAuthenticatedUser(authenticatedUser)

    fun addUserAPIKey(userId: Long) = repo.addUserAPIKey(webSocket, userId)
}