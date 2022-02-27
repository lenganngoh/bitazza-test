package gohlengann.apps.bitazza_test.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import gohlengann.apps.bitazza_test.data.model.entity.Instrument
import gohlengann.apps.bitazza_test.databinding.AdapterInstrumentBinding

class InstrumentRecyclerViewAdapter :
    RecyclerView.Adapter<InstrumentRecyclerViewAdapter.ViewHolder>() {

    private var instruments: MutableList<Instrument> = mutableListOf()

    fun setInstruments(instruments: MutableList<Instrument>) {
        val diffCallback = InstrumentDiffCallback(this.instruments, instruments)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.instruments = instruments
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InstrumentRecyclerViewAdapter.ViewHolder {
        return ViewHolder(
            AdapterInstrumentBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: InstrumentRecyclerViewAdapter.ViewHolder, position: Int) {
        with(holder.binding) {
            val instrument = instruments[position]
            name = instrument.symbol
            currentDayPxChange = String.format("%.2f", instrument.current_day_px_change) + "%"
            lastTradedPx = String.format("฿%.2f", instrument.last_traded_px)
            bestOffer = String.format("฿%.2f", instrument.best_offer)
            flPercent.isActivated = instrument.current_day_px_change < 0
        }
    }

    override fun getItemCount(): Int = instruments.size

    inner class ViewHolder(var binding: AdapterInstrumentBinding) :
        RecyclerView.ViewHolder(binding.root)
}

class InstrumentDiffCallback(
    private val oldList: List<Instrument>,
    private val newList: List<Instrument>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].instrument_id == newList[newItemPosition].instrument_id
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return false
    }

    @Nullable
    override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
        return super.getChangePayload(oldPosition, newPosition)
    }
}
