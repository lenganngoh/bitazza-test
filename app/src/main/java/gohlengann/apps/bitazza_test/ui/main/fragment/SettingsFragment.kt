package gohlengann.apps.bitazza_test.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import gohlengann.apps.bitazza_test.databinding.FragmentLogoutBinding

class SettingsFragment(private val callback: Callback) : Fragment() {

    private lateinit var binding: FragmentLogoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogoutBinding.inflate(inflater)

        setupListeners()

        return binding.root
    }

    private fun setupListeners() {
        binding.btnLogout.setOnClickListener {
            callback.logout()
        }
    }

    interface Callback {
        fun logout()
    }
}