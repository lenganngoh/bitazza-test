package gohlengann.apps.bitazza_test.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import gohlengann.apps.bitazza_test.data.model.response.LogoutResponse
import gohlengann.apps.bitazza_test.data.model.request.MessageAction
import gohlengann.apps.bitazza_test.data.model.response.SubscribeResponse
import gohlengann.apps.bitazza_test.data.model.entity.Instrument
import gohlengann.apps.bitazza_test.data.remote.Functions
import gohlengann.apps.bitazza_test.data.remote.WSListener
import gohlengann.apps.bitazza_test.data.remote.repository.MainRepository
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okio.ByteString

class MainViewModel @ViewModelInject constructor(
    private val repo: MainRepository,
    private val gson: Gson
) : ViewModel() {

    private lateinit var webSocket: WebSocket

    val isWebSocketOpen: MutableLiveData<Boolean> = MutableLiveData(false)
    val cachedInstruments: MutableLiveData<List<Instrument>?> = MutableLiveData(null)
    val shouldLogout: MutableLiveData<Boolean> = MutableLiveData(false)

    private val callback: WSListener.WSCallback = object : WSListener.WSCallback {
        override fun connectionOpened(webSocket: WebSocket) {
            this@MainViewModel.webSocket = webSocket
            viewModelScope.launch {
                isWebSocketOpen.value = true
            }
        }

        override fun onServerResponse(webSocket: WebSocket, text: String) {
            this@MainViewModel.webSocket = webSocket
            val rawResponse = gson.fromJson(text, MessageAction::class.java)
            when (rawResponse.n) {
                Functions.GET_INSTRUMENTS.function -> {
                    val response =
                        gson.fromJson(rawResponse.o, Array<Instrument>::class.java).toList()
                    clearInstruments()
                    response.forEach {
                        insertInstrument(it)
                    }
                    viewModelScope.launch {
                        cachedInstruments.value = response
                    }
                }

                Functions.SUBSCRIBE.function -> {
                    val response = gson.fromJson(rawResponse.o, SubscribeResponse::class.java)
                    // TODO cannot get response from API
                }

                Functions.LOGOUT.function -> {
                    val response = gson.fromJson(rawResponse.o, LogoutResponse::class.java)
                    if (response.result) {
                        clearAuthenticatedUser()
                    }
                    viewModelScope.launch {
                        shouldLogout.value = response.result
                    }
                }
            }
        }

        override fun onServerResponse(webSocket: WebSocket, text: ByteString) {}

        override fun connectionFailed(webSocket: WebSocket, t: Throwable, response: Response?) {}
    }

    fun openWebSocket() = repo.openWebSocket(callback)

    fun getAuthenticatedUser() = repo.getAuthenticatedUser()

    fun clearAuthenticatedUser() = repo.clearAuthenticatedUser()

    /**
     * Instruments
     */
    fun getInstruments(omsId: Long) = repo.getInstruments(webSocket, omsId)

    fun getLocalInstruments() = repo.getLocalInstruments()

    fun insertInstrument(instrument: Instrument) = repo.insertInstrument(instrument)

    fun clearInstruments() = repo.clearInstruments()

    fun updateInstrument(
        instrumentId: Long,
        volume: Double,
        bestOffer: Double,
        lastTradedPx: Double,
        currentDayPxChange: Double
    ) = repo.updateInstrument(instrumentId, volume, bestOffer, lastTradedPx, currentDayPxChange)

    /**
     * Subscribe
     */
    fun subscribe(omsId: Long, sessionNumber: Long, instrumentId: Long) =
        repo.subscribe(webSocket, omsId, sessionNumber, instrumentId)

    fun getAccountPositions(accountId: Long, omsId: Long) =
        repo.getAccountPositions(webSocket, accountId, omsId)

    /**
     * Logout
     */
    fun logout() = repo.logout(webSocket)

}