package com.shubinat.notforgot.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.shubinat.notforgot.databinding.FragmentNoteDetailsBinding
import com.shubinat.notforgot.domain.entity.Note
import com.shubinat.notforgot.presentation.viewmodelfactories.DetailsViewModelFactory
import com.shubinat.notforgot.presentation.viewmodels.DetailsViewModel


class NoteDetailsFragment : Fragment() {

    private var _binding: FragmentNoteDetailsBinding? = null
    private val binding: FragmentNoteDetailsBinding
        get() = _binding ?: throw RuntimeException("FragmentNoteDetailsBinding == null")

    private val args by navArgs<NoteDetailsFragmentArgs>()

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            DetailsViewModelFactory(args.selectedNoteId, requireActivity().application)
        )[DetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.load()
        binding.viewModel = viewModel
        setupEditButtonListener()
    }


    private fun setupEditButtonListener() {
        binding.buttonEdit.setOnClickListener {
            val note = viewModel.note.get() as Note
            findNavController().navigate(NoteDetailsFragmentDirections.actionNoteDetailsFragmentToNoteEditorFragment(
                note.user,
                note))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}