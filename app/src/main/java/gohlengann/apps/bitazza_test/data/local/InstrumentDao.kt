package gohlengann.apps.bitazza_test.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import gohlengann.apps.bitazza_test.data.model.entity.Instrument

@Dao
interface InstrumentDao {
    @Query("SELECT * FROM instrument")
    fun getLocalInstruments(): LiveData<List<Instrument>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInstrument(instrument: Instrument)

    @Query("DELETE FROM instrument")
    fun clearInstruments()

    @Query("UPDATE instrument SET volume = :volume, best_offer = :bestOffer, last_traded_px =:lastTradedPx, current_day_px_change =:currentDayPxChange WHERE instrument_id = :instrumentId")
    fun updateInstrument(
        instrumentId: Long,
        volume: Double,
        bestOffer: Double,
        lastTradedPx: Double,
        currentDayPxChange: Double
    )
}