package gohlengann.apps.bitazza_test.data.model.response

import com.google.gson.annotations.SerializedName

data class SubscribeResponse(
    @SerializedName("BestOffer") val best_offer: Double,
    @SerializedName("LastTradedPx") val last_traded_px: Double,
    @SerializedName("Volume") val volume: Double,
    @SerializedName("CurrentDayPxChange") val current_day_px_change: Double
)