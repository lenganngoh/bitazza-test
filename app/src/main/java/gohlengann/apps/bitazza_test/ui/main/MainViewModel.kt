package gohlengann.apps.bitazza_test.ui.main

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import gohlengann.apps.bitazza_test.data.model.entity.AuthenticatedUser
import gohlengann.apps.bitazza_test.data.model.response.LogoutResponse
import gohlengann.apps.bitazza_test.data.model.request.MessageAction
import gohlengann.apps.bitazza_test.data.model.response.SubscribeResponse
import gohlengann.apps.bitazza_test.data.model.entity.Instrument
import gohlengann.apps.bitazza_test.data.model.entity.Product
import gohlengann.apps.bitazza_test.data.remote.Functions
import gohlengann.apps.bitazza_test.data.remote.WSListener
import gohlengann.apps.bitazza_test.data.remote.repository.MainRepository
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import okhttp3.WebSocket
import okio.ByteString
import okhttp3.Response

class MainViewModel @ViewModelInject constructor(
    private val repo: MainRepository,
    private val gson: Gson
) : ViewModel() {

    private lateinit var webSocket: WebSocket
    private val poll = Channel<Deferred<Boolean>>()
    var isProductPolling = false

    val isWebSocketOpen: MutableLiveData<Boolean> = MutableLiveData(false)
    val cachedUser: MutableLiveData<AuthenticatedUser?> = MutableLiveData(null)
    val cachedInstruments: MutableLiveData<List<Instrument>?> = MutableLiveData(null)
    val cachedProducts: MutableLiveData<List<Product>?> = MutableLiveData(null)
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
                    response.forEach {
                        if (it.product2_symbol.equals("BTC", true)) {
                            insertInstrument(it)
                        }
                    }
                    viewModelScope.launch {
                        cachedInstruments.value = response
                    }
                }

                Functions.SUBSCRIBE.function -> {
                    val response = gson.fromJson(rawResponse.o, SubscribeResponse::class.java)
                    // TODO cannot get response from API
                }

                Functions.GET_PRODUCTS.function -> {
                    val response = gson.fromJson(rawResponse.o, Array<Product>::class.java).toList()
                    clearProducts()
                    response.forEach {
                        insertProduct(it)
                    }
                    viewModelScope.launch {
                        cachedProducts.value = response
                    }
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

    fun isWebSocketReady() = this::webSocket.isInitialized

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
     * Products
     */
    fun getProducts(omsId: Long) = repo.getProducts(webSocket, omsId)

    fun getLocalProducts() = repo.getLocalProducts()

    fun insertProduct(product: Product) = repo.insertProduct(product)

    fun clearProducts() = repo.clearProducts()

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

    /**
     * Level1 Summary
     */
    fun getLevel1Summary(omsId: Long, baseCurrency: String) {
        repo.getLevel1Summary(omsId, baseCurrency).subscribeOn(Schedulers.io())
            .subscribe(object : DisposableSingleObserver<retrofit2.Response<List<List<String>>>>() {
                override fun onSuccess(response: retrofit2.Response<List<List<String>>>) {
                    if (response.errorBody() == null) {
                        response.body()?.forEach { _response ->
                            updateInstrument(
                                _response[0].toLong(),
                                _response[5].toDouble(),
                                _response[2].toDouble() + _response[3].toDouble(),
                                _response[2].toDouble(),
                                _response[4].toDouble()
                            )
                        }
                        triggerPoll()
                    }
                }

                override fun onError(e: Throwable?) {
                    e?.let {

                    }
                }
            })
    }

    /**
     * Polling
     */
    fun startProductPoll(omsId: Long) {
        isProductPolling = true
        CoroutineScope(Dispatchers.IO).launch {
            for (i in poll) {
                i.await()
                getLevel1Summary(omsId, "BTC")
            }
        }
    }

    private fun triggerPoll() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            poll.send(async {
                true
            })
        }
    }
}