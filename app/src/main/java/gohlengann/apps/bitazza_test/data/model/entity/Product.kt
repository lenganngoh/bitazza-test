package gohlengann.apps.bitazza_test.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "product")
data class Product(
    @PrimaryKey @SerializedName("ProductId") val product_id: Long = 0,
    @SerializedName("OMSId") val oms_id: Long = 0,
    @SerializedName("Product") val product: String = "",
    @SerializedName("ProductFullName") val product_full_name: String = "",
    @SerializedName("ProductType") val product_type: String = "",
    @SerializedName("DecimalPlaces") val decimal_places: Double = 0.0,
    @SerializedName("TickSize") val tick_size: Double = 0.0,
    @SerializedName("NoFees") val has_no_fee: Boolean = false
)