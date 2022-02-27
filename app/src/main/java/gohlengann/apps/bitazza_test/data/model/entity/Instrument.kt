package gohlengann.apps.bitazza_test.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "instrument")
data class Instrument(
    @PrimaryKey @SerializedName("InstrumentId") val instrument_id: Long = 0,
    @SerializedName("OMSId") val oms_id: Long = 0,
    @SerializedName("Symbol") val symbol: String = "",
    @SerializedName("Product1") val product1: Long = 0,
    @SerializedName("Product1Symbol") val product1_symbol: String = "",
    @SerializedName("Product2") val product2: Long = 0,
    @SerializedName("Product2Symbol") val product2_symbol: String = "",
    @SerializedName("InstrumentType") val instrument_type: String = "",
    @SerializedName("VenueInstrumentId") val venue_instrument_id: Long = 0,
    @SerializedName("VenueId") val venue_id: Long = 0,
    @SerializedName("SortIndex") val sortIndex: Long = 0,
    @SerializedName("SessionStatus") val session_status: String = "",
    @SerializedName("PreviousSessionStatus") val previous_session_status: String = "",
    @SerializedName("SessionStatusDateTime") val session_status_date_time: String = "",
    @SerializedName("QuantityIncrement") val quantity_increment: Double = 0.0,
    @SerializedName("PriceIncrement") val price_increment: Float = 0.0f,
    @SerializedName("Volume") val volume: Float = 0.0f,
    @SerializedName("LastTradedPx") val last_traded_px: Float = 0.0f,
    @SerializedName("BestOffer") val best_offer: Float = 0.0f,
    @SerializedName("CurrentDayPxChange") val current_day_px_change: Float = 0.0f
)