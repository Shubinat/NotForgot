package com.shubinat.notforgot.presentation.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.shubinat.notforgot.R
import com.shubinat.notforgot.databinding.FragmentAddCategoryDialogBinding
import com.shubinat.notforgot.databinding.FragmentLoginBinding
import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.presentation.viewmodelfactories.AddCategoryViewModelFactory
import com.shubinat.notforgot.presentation.viewmodels.AddCategoryViewModel
import java.util.IllegalFormatCodePointException

class AddCategoryDialogFragment : DialogFragment() {
    private var _binding: FragmentAddCategoryDialogBinding? = null
    private val binding: FragmentAddCategoryDialogBinding
        get() = _binding ?: throw RuntimeException("FragmentAddCategoryDialogBinding == null")

    private val authUser: User by lazy {
        if (arguments == null)
            throw RuntimeException("Args is NULL")
        if (!requireArguments().containsKey(AUTH_USER_KEY))
            throw RuntimeException("AuthUser is NULL")
        requireArguments().getParcelable<User>(AUTH_USER_KEY)!!
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            AddCategoryViewModelFactory(authUser)
        )[AddCategoryViewModel::class.java]
    }

    var dialogClosedListener: DialogClosedListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = FragmentAddCategoryDialogBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        return AlertDialog.Builder(requireContext())
            .setNegativeButton(R.string.add_category_button_cancel) { _, _ ->
                dialogClosedListener?.dialogClosed(
                    null
                )
            }
            .setPositiveButton(R.string.add_category_button_ok) { _, _ ->
                dialogClosedListener?.dialogClosed(viewModel.save())
            }
            .setView(binding.root)
            .create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCategoryDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newAddCategoryDialogFragment(
            authUser: User,
            closedListener: DialogClosedListener
        ): AddCategoryDialogFragment {
            return AddCategoryDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(AUTH_USER_KEY, authUser)
                dialogClosedListener = closedListener
                }
            }
        }

        private const val AUTH_USER_KEY = "AuthUser"
        const val TAG = "AddCategoryDialog"
    }

    interface DialogClosedListener {
        fun dialogClosed(success: Boolean?)
    }
}