package gohlengann.apps.bitazza_test.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import gohlengann.apps.bitazza_test.data.model.entity.Instrument
import gohlengann.apps.bitazza_test.databinding.FragmentMarketBinding
import gohlengann.apps.bitazza_test.ui.main.adapter.InstrumentRecyclerViewAdapter

class MarketFragment : Fragment() {

    private lateinit var binding: FragmentMarketBinding
    private lateinit var instrumentsAdapter: InstrumentRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMarketBinding.inflate(inflater)

        setupInstrumentsAdapter()

        return binding.root
    }

    private fun setupInstrumentsAdapter() {
        instrumentsAdapter = InstrumentRecyclerViewAdapter()
        binding.rvMarket.layoutManager = LinearLayoutManager(context)
        binding.rvMarket.adapter = instrumentsAdapter
    }

    fun updateInstrumentsAdapter(instruments: MutableList<Instrument>) {
        if (this::instrumentsAdapter.isInitialized) {
            instrumentsAdapter.setInstruments(instruments)
        }
    }
}