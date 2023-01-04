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
import com.shubinat.notforgot.databinding.FragmentMainBinding
import com.shubinat.notforgot.presentation.adapters.NotesAdapter
import com.shubinat.notforgot.presentation.viewmodels.MainViewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding ?: throw RuntimeException("FragmentMainBinding == null")

    private val args by navArgs<MainFragmentArgs>()

    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        setupAddClickListener()
        setupLoadingDialog()
        setupRecycleView()
    }

    private fun setupLoadingDialog() {
        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                val dialog = LoadingDialogFragment.newInstance(getString(R.string.message_sync))
                dialog.show(parentFragmentManager, LoadingDialogFragment.TAG)
            } else {
                val fragment = parentFragmentManager.findFragmentByTag(LoadingDialogFragment.TAG)
                if (fragment != null) {
                    val dialog = fragment as LoadingDialogFragment
                    dialog.dismiss()
                }
            }
        }
    }

    private fun setupAddClickListener() {
        binding.buttonAdd.setOnClickListener {
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToNoteEditorFragment(
                    args.authUser,
                    null
                )
            )
        }
    }

    private fun setupRecycleView() {
        viewModel.getAllNotes(args.authUser)
        viewModel.notes.observe(viewLifecycleOwner) {
            binding.tvEmptyList.visibility = if (it.isNotEmpty()) View.GONE else View.VISIBLE
            val adapter = NotesAdapter(it)
            setupCheckBoxClick(adapter)
            setupItemLongClick(adapter)
            binding.recyclerViewNotes.adapter = adapter
        }

    }

    private fun setupCheckBoxClick(adapter: NotesAdapter) {
        adapter.onItemCheckBoxClickListener = {
            viewModel.changeCompletedStatus(it)
        }
    }

    private fun setupItemLongClick(adapter: NotesAdapter) {
        adapter.onItemLongClickListener = {
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToNoteDetailsFragment(
                    it.id
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}