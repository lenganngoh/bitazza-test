package gohlengann.apps.bitazza_test.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import dagger.hilt.android.AndroidEntryPoint
import gohlengann.apps.bitazza_test.R
import gohlengann.apps.bitazza_test.data.model.entity.AuthenticatedUser
import gohlengann.apps.bitazza_test.databinding.ActivityMainBinding
import gohlengann.apps.bitazza_test.ui.login.LoginActivity
import gohlengann.apps.bitazza_test.ui.main.adapter.BottomNavigationFragmentViewPagerAdapter
import gohlengann.apps.bitazza_test.ui.main.fragment.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val vm: MainViewModel by viewModels()

    private var cachedAuthenticatedUser: AuthenticatedUser? = null

    /**
     * Fragment Callbacks
     */
    private val settingsFragmentCallback = object : SettingsFragment.Callback {
        override fun logout() {
            vm.logout()
        }
    }

    /**
     * Fragments
     */
    private val marketFragment = MarketFragment()
    private val walletFragment = WalletFragment()
    private val newsFragment = NewsFragment()
    private val activityFragment = ActivityFragment()
    private val settingsFragment = SettingsFragment(settingsFragmentCallback)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapters()
        setupObservers()
        setupListeners()
    }

    private fun setupAdapters() {
        binding.vpFragment.adapter = BottomNavigationFragmentViewPagerAdapter(
            object : BottomNavigationFragmentViewPagerAdapter.Callback {
                override fun getMarketFragment(): Fragment {
                    return marketFragment
                }

                override fun getWalletFragment(): Fragment {
                    return walletFragment
                }

                override fun getNewsFragment(): Fragment {
                    return newsFragment
                }

                override fun getActivityFragment(): Fragment {
                    return activityFragment
                }

                override fun getSettingsFragment(): Fragment {
                    return settingsFragment
                }

            }, supportFragmentManager
        )
    }

    private fun setupObservers() {
        vm.isWebSocketOpen.observe(this) {
            if (it && cachedAuthenticatedUser != null) {
                vm.getInstruments(cachedAuthenticatedUser!!.oms_id)
            }
        }

        vm.getAuthenticatedUser().observe(this) {
            cachedAuthenticatedUser = it
            it?.let {
                vm.openWebSocket()
            }
        }

        vm.cachedInstruments.observe(this) {
            it?.let {
                marketFragment.updateInstrumentsAdapter(it.toMutableList())
            }

            if (cachedAuthenticatedUser != null) {
                var sessionNumber: Long = 2
                it?.forEach { instrument ->
                    vm.subscribe(
                        cachedAuthenticatedUser!!.oms_id,
                        sessionNumber,
                        instrument.instrument_id
                    )
                    sessionNumber += 2
                }
            }
        }

        vm.shouldLogout.observe(this) {
            if (it) {
                openLoginActivity()
            }
        }
    }

    private fun setupListeners() {
        binding.bottomNavigationBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_market -> {
                    binding.vpFragment.currentItem = 0
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.menu_wallet -> {
                    binding.vpFragment.currentItem = 1
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.menu_news -> {
                    binding.vpFragment.currentItem = 2
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.menu_activity -> {
                    binding.vpFragment.currentItem = 3
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.menu_settings -> {
                    binding.vpFragment.currentItem = 4
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                }
            }
            return@setOnNavigationItemSelectedListener false
        }
    }

    private fun openLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}