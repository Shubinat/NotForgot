package com.shubinat.notforgot.presentation.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.shubinat.notforgot.R
import com.shubinat.notforgot.databinding.FragmentLoadingDialogBinding

class LoadingDialogFragment : DialogFragment() {

    private lateinit var message: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = FragmentLoadingDialogBinding.inflate(layoutInflater)
        binding.tvMessage.text = message
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.setCanceledOnTouchOutside(false)
        isCancelable = false
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_loading_dialog, container, false)
    }

    private fun parseArguments() {
        arguments ?: throw RuntimeException("Arguments is null")
        if (requireArguments().containsKey(MESSAGE_KEY)) {
            message = requireArguments().getString(MESSAGE_KEY) ?: DEFAULT_MESSAGE_VALUE
        } else{
            throw RuntimeException("The key $MESSAGE_KEY does not exist")
        }
    }

    companion object {

        const val MESSAGE_KEY = "Message"
        const val TAG = "LoadingDialogFragment"
        private const val DEFAULT_MESSAGE_VALUE = ""

        @JvmStatic
        fun newInstance(message: String) =
            LoadingDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(MESSAGE_KEY, message)
                }
            }
    }
}