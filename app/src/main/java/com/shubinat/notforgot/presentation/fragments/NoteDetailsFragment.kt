package com.shubinat.notforgot.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.shubinat.notforgot.R
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
        observeNote()
        setupLoadingScreen()
        setupEditButtonListener()
        viewModel.load()
        binding.viewModel = viewModel

    }

    private fun setupLoadingScreen() {
        viewModel.loading.observe(viewLifecycleOwner) {
            val mainVisibility = if (it) View.GONE else View.VISIBLE
            val loadingVisibility = if (it) View.VISIBLE else View.GONE
            binding.layoutMain.visibility = mainVisibility
            binding.layoutLoading.visibility = loadingVisibility
        }
    }

    private fun observeNote() {
        viewModel.note.observe(viewLifecycleOwner) {
            with(binding) {
                tvTitle.text = it.title
                tvDescription.text = it.description
                tvCreationDate.text = it.creationDate.toString()
                tvCompletionDate.text = it.completionDate.toString()
                tvCompleted.text =
                    if (it.completed) getString(R.string.details_completed)
                    else getString(R.string.details_not_completed)
                val colorId =
                    if (it.completed) android.R.color.holo_green_light
                    else android.R.color.holo_red_light
                tvCompleted.setTextColor(resources.getColor(colorId))
            }
        }
    }


    private fun setupEditButtonListener() {
        binding.buttonEdit.setOnClickListener {
            val note = viewModel.note.value as Note
            findNavController().navigate(
                NoteDetailsFragmentDirections.actionNoteDetailsFragmentToNoteEditorFragment(
                    note.user,
                    note
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}