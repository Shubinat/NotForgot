package com.shubinat.notforgot.presentation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shubinat.notforgot.R
import com.shubinat.notforgot.databinding.FragmentLoginBinding
import com.shubinat.notforgot.databinding.FragmentRegistrationBinding
import com.shubinat.notforgot.presentation.viewmodels.RegistrationViewModel

class RegistrationFragment : Fragment(), RegistrationViewModel.SuccessRegistrationListener {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding: FragmentRegistrationBinding
        get() = _binding ?: throw RuntimeException("FragmentRegistrationBinding == null")

    private val viewModel by lazy {
        ViewModelProvider(this)[RegistrationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        viewModel.successRegistrationListener = this
        observeViewModel()
        registerTextChangedListeners()
        setupLoadingDialog()
    }

    private fun setupLoadingDialog() {
        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                val dialog = LoadingDialogFragment.newInstance(getString(R.string.message_register))
                dialog.show(parentFragmentManager, LoadingDialogFragment.TAG)
            } else{
                val prev = parentFragmentManager.findFragmentByTag(LoadingDialogFragment.TAG)
                if (prev != null) {
                    val dialog = prev as LoadingDialogFragment
                    dialog.dismiss()
                }
            }
        }
    }
    private fun observeViewModel() {
        viewModel.userNameError.observe(viewLifecycleOwner) {
            binding.tilName.error = if (it) "Поле не может быть пустым" else null
        }
        viewModel.loginError.observe(viewLifecycleOwner) {
            binding.tilEmail.error = if (it) "Поле не может быть пустым" else null
        }
        viewModel.passwordError.observe(viewLifecycleOwner) {
            binding.tilPassword.error = if (it) "Поле не может быть пустым" else null
        }

        viewModel.retryPasswordError.observe(viewLifecycleOwner) {
            binding.tilRetryPassword.error = if (it) "Пароли не совпадают" else null
        }
    }

    private fun registerTextChangedListeners() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.resetUserNameError()
            }

        })

        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.resetLoginError()
            }

        })

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.resetPasswordError()
            }

        })

        binding.etRetryPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.resetRetryPasswordError()
            }

        })
    }

    override fun successRegistration() {
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}