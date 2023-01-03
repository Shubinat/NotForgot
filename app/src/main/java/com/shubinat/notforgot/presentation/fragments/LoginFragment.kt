package com.shubinat.notforgot.presentation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shubinat.notforgot.R
import com.shubinat.notforgot.databinding.FragmentLoginBinding
import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.presentation.viewmodels.LoginViewModel


class LoginFragment : Fragment(), LoginViewModel.SuccessAuthorizationListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding ?: throw RuntimeException("FragmentLoginBinding == null")


    private val viewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    private fun observeViewModel() {
        viewModel.loginError.observe(viewLifecycleOwner) {
            binding.tilEmail.error = if (it) getString(R.string.login_text_input_error) else null
        }
        viewModel.passwordError.observe(viewLifecycleOwner) {
            binding.tilPassword.error = if (it) getString(R.string.login_text_input_error) else null
        }
        viewModel.passwordError.observe(viewLifecycleOwner) {
            binding.tilPassword.error = if (it) getString(R.string.login_text_input_error) else null
        }
    }

    private fun addTextChangeListeners() {
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.resetLoginError()
            }

        }
        )

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.resetPasswordError()
            }
        }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        viewModel.successAuthorizationListener = this
        observeViewModel()
        addTextChangeListeners()
        addClickListeners()
        setupLoadingDialog()
    }

    private fun setupLoadingDialog() {
        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                val dialog = LoadingDialogFragment.newInstance(getString(R.string.message_auth))
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

    private fun addClickListeners() {
        binding.buttonReg.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun successAuthorization(user: User) {
        findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToMainFragment(user)
        )
    }
}