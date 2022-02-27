package gohlengann.apps.bitazza_test.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.observe
import dagger.hilt.android.AndroidEntryPoint
import gohlengann.apps.bitazza_test.databinding.ActivityLoginBinding
import gohlengann.apps.bitazza_test.ui.main.MainActivity
import gohlengann.apps.bitazza_test.util.gone
import gohlengann.apps.bitazza_test.util.hideKeyboard

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val vm: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        vm.isWebSocketOpen.observe(this) {
            if (it) {
                binding.pbLoading.gone()
            }
        }

        vm.getAuthenticatedUser().observe(this) {
            if (it != null) {
                openMainActivity()
            } else {
                vm.openWebSocket()
            }
        }
    }

    private fun openMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            if (!binding.etUser.text.isNullOrEmpty() || !binding.etPass.text.isNullOrEmpty()) {
                it.isEnabled = false
                hideKeyboard()
                vm.authenticate(
                    binding.etUser.text.toString(),
                    binding.etPass.text.toString()
                )
            }
        }
    }
}