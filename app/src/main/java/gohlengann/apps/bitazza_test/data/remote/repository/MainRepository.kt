package gohlengann.apps.bitazza_test.data.remote.repository

import com.google.gson.Gson
import gohlengann.apps.bitazza_test.data.local.AuthenticatedUserDao
import gohlengann.apps.bitazza_test.data.local.InstrumentDao
import gohlengann.apps.bitazza_test.data.local.ProductDao
import gohlengann.apps.bitazza_test.data.model.entity.Instrument
import gohlengann.apps.bitazza_test.data.model.entity.Product
import gohlengann.apps.bitazza_test.data.model.request.*
import gohlengann.apps.bitazza_test.data.remote.Functions
import gohlengann.apps.bitazza_test.data.remote.WSListener
import gohlengann.apps.bitazza_test.data.remote.service.Level1SummaryService
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val request: Request,
    private val gson: Gson,
    private val authenticatedUserDao: AuthenticatedUserDao,
    private val instrumentDao: InstrumentDao,
    private val productDao: ProductDao,
    private val level1SummaryService: Level1SummaryService
) {
    private lateinit var listener: WSListener

    fun openWebSocket(callback: WSListener.WSCallback) {
        listener = WSListener(callback)
        okHttpClient.newWebSocket(request, listener)
        okHttpClient.dispatcher.executorService.shutdown()
    }

    fun addUserMarketDataPermission(
        webSocket: WebSocket,
        omsId: Long,
        userId: Long,
        instrumentId: Long
    ) {
        webSocket.send(
            gson.toJson(
                MessageAction(
                    0,
                    0,
                    Functions.ADD_USER_INSTRUMENT_PERMISSION.function,
                    gson.toJson(
                        AddUserInstrumentPermissionRequest(
                            omsId,
                            instrumentId,
                            listOf(userId)
                        )
                    )
                )
            )
        )
    }

    fun getInstruments(webSocket: WebSocket, omsId: Long) {
        webSocket.send(
            gson.toJson(
                MessageAction(
                    0,
                    0,
                    Functions.GET_INSTRUMENTS.function,
                    gson.toJson(GetInstrumentRequest(omsId))
                )
            )
        )
    }

    fun subscribe(webSocket: WebSocket, omsId: Long, sessionNumber: Long, instrumentId: Long) {
        webSocket.send(
            gson.toJson(
                MessageAction(
                    2,
                    sessionNumber,
                    Functions.SUBSCRIBE.function,
                    gson.toJson(SubscribeRequest(omsId, instrumentId))
                )
            )
        )
    }

    fun getAuthenticatedUser() = authenticatedUserDao.getAuthenticatedUser()

    fun clearAuthenticatedUser() = authenticatedUserDao.clearAuthenticatedUser()

    fun getLocalInstruments() = instrumentDao.getLocalInstruments()

    fun insertInstrument(instrument: Instrument) = instrumentDao.insertInstrument(instrument)

    fun clearInstruments() = instrumentDao.clearInstruments()

    fun updateInstrument(
        instrumentId: Long,
        volume: Double,
        bestOffer: Double,
        lastTradedPx: Double,
        currentDayPxChange: Double
    ) = instrumentDao.updateInstrument(
        instrumentId,
        volume,
        bestOffer,
        lastTradedPx,
        currentDayPxChange
    )

    fun getAccountPositions(webSocket: WebSocket, accountId: Long, omsId: Long) {
        webSocket.send(
            gson.toJson(
                MessageAction(
                    0,
                    0,
                    Functions.GET_ACCOUNT_POSITIONS.function,
                    gson.toJson(GetAccountPositionRequest(accountId, omsId))
                )
            )
        )
    }

    fun getProducts(webSocket: WebSocket, omsId: Long) {
        webSocket.send(
            gson.toJson(
                MessageAction(
                    0,
                    0,
                    Functions.GET_PRODUCTS.function,
                    gson.toJson(GetInstrumentRequest(omsId))
                )
            )
        )
    }

    fun getLocalProducts() = productDao.getLocalProducts()

    fun insertProduct(product: Product) = productDao.insertProduct(product)

    fun clearProducts() = productDao.clearProducts()

    fun logout(webSocket: WebSocket) {
        webSocket.send(
            gson.toJson(
                MessageAction(
                    0,
                    0,
                    Functions.LOGOUT.function,
                    "{}"
                )
            )
        )
    }

    fun getLevel1Summary(omsId: Long, baseCurrency: String) = level1SummaryService.getLevel1Summary(omsId, baseCurrency)
}