package com.shubinat.notforgot.presentation.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.shubinat.notforgot.R
import com.shubinat.notforgot.databinding.FragmentNoteEditorBinding
import com.shubinat.notforgot.presentation.viewmodelfactories.EditorViewModelFactory
import com.shubinat.notforgot.presentation.viewmodels.EditorViewModel
import java.util.Calendar

class NoteEditorFragment : Fragment(), EditorViewModel.SelectDateListener,
    EditorViewModel.AddCategoryListener,
    AddCategoryDialogFragment.DialogClosedListener {

    private val args by navArgs<NoteEditorFragmentArgs>()
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            EditorViewModelFactory(
                requireActivity().application,
                args.authUser,
                args.editableNote
            )
        )[EditorViewModel::class.java]
    }
    private var _binding: FragmentNoteEditorBinding? = null
    private val binding: FragmentNoteEditorBinding
        get() = _binding ?: throw RuntimeException("FragmentNoteEditorBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        viewModel.selectDateListener = this
        viewModel.addCategoryListener = this

        setupCategoriesSpinner()
        setupPrioritiesSpinner()
        setupSaveClickListener()
        bindErrorMessages()
        setupTextChangedListeners()
    }


    private fun setupTextChangedListeners() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.resetNameError()
            }

        })

        binding.etDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.resetDescriptionError()
            }

        })

        binding.etCompletionDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.resetDateError()
            }

        })

    }

    private fun bindErrorMessages() {
        viewModel.nameError.observe(viewLifecycleOwner) {
            binding.tilName.error = if (it == true) getString(R.string.editor_name_error) else null
        }
        viewModel.descriptionError.observe(viewLifecycleOwner) {
            binding.tilDescription.error =
                if (it == true) getString(R.string.editor_description_error) else null
        }
        viewModel.dateError.observe(viewLifecycleOwner) {
            binding.tilDate.error =
                if (it == true) getString(R.string.editor_date_error) else null
        }
        viewModel.priorityError.observe(viewLifecycleOwner) {
            if (it == true) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.editor_priority_error),
                    Toast.LENGTH_LONG
                ).show()

            }
        }
    }

    private fun setupSaveClickListener() {
        binding.buttonSave.setOnClickListener {
            if (viewModel.save()) {
                findNavController().popBackStack()
            }
        }
    }

    private fun setupCategoriesSpinner() {
        viewModel.categories.observe(viewLifecycleOwner) {
            val categoryAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item, it
            )
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCategory.adapter = categoryAdapter
        }
    }

    private fun setupPrioritiesSpinner() {
        viewModel.priorities.observe(viewLifecycleOwner) {
            val priorityAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item, it
            )
            priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerPriority.adapter = priorityAdapter
        }
    }

    override fun selectDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val dialog = DatePickerDialog(
            requireContext(),
            { _, y, m, d ->
                run {
                    binding.etCompletionDate.setText(
                        requireActivity().getString(R.string.date_format)
                            .format(y, m + 1, d)
                    )
                }
            },
            year,
            month,
            dayOfMonth
        )
        dialog.show()
    }

    override fun addCategory() {
        val dialog = AddCategoryDialogFragment.newAddCategoryDialogFragment(args.authUser, this)
        dialog.show(parentFragmentManager, AddCategoryDialogFragment.TAG)
    }

    override fun dialogClosed(success: Boolean?) {
        if (success == true) {
            viewModel.loadCategoriesNames()
        } else if (success == false) {
            Toast.makeText(
                requireContext(),
                requireActivity().getString(R.string.add_category_error),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}