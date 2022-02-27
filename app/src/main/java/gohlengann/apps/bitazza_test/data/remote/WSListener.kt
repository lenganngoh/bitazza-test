package gohlengann.apps.bitazza_test.data.remote

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class WSListener constructor(private val callback: WSCallback) : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        callback.connectionOpened(webSocket)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        callback.onServerResponse(webSocket, text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        callback.onServerResponse(webSocket, bytes)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(CLOSE_STATUS, null)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        callback.connectionFailed(webSocket, t, response)
    }

    companion object {
        private const val CLOSE_STATUS = 1000
    }

    interface WSCallback {
        fun connectionOpened(webSocket: WebSocket)
        fun onServerResponse(webSocket: WebSocket, text: String)
        fun onServerResponse(webSocket: WebSocket, text: ByteString)
        fun connectionFailed(webSocket: WebSocket, t: Throwable, response: Response?)
    }
}