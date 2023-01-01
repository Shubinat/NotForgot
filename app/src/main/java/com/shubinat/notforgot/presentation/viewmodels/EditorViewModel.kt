package com.shubinat.notforgot.presentation.viewmodels

import android.app.Application
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shubinat.notforgot.data.local.repository.CategoryRepositoryImpl
import com.shubinat.notforgot.data.local.repository.NoteRepositoryImpl
import com.shubinat.notforgot.domain.entity.Category
import com.shubinat.notforgot.domain.entity.Note
import com.shubinat.notforgot.domain.entity.Priority
import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.domain.usecases.categories.GetAllCategoriesUseCase
import com.shubinat.notforgot.domain.usecases.notes.AddNoteUseCase
import com.shubinat.notforgot.domain.usecases.notes.EditNoteUseCase
import java.time.LocalDate


class EditorViewModel(
    private val app: Application,
    private val authUser: User,
    private val note: Note? = null
) :
    AndroidViewModel(app) {

    private val categoryRepository = CategoryRepositoryImpl
    private val noteRepository = NoteRepositoryImpl

    private val getAllCategoriesUseCase = GetAllCategoriesUseCase(categoryRepository)
    private val addNoteUseCase = AddNoteUseCase(noteRepository)
    private val editNoteUseCase = EditNoteUseCase(noteRepository)

    var selectDateListener: SelectDateListener? = null
    var addCategoryListener: AddCategoryListener? = null


    val editMode: Boolean
        get() = note != null

    val name: ObservableField<String> =
        ObservableField(note?.title ?: DEFAULT_NAME_VALUE)

    val description: ObservableField<String> =
        ObservableField(note?.description ?: DEFAULT_DESCRIPTION_VALUE)

    val date: ObservableField<String> = if (editMode) {
        ObservableField(note!!.completionDate.toString())
    } else {
        ObservableField(DEFAULT_DATE_VALUE)
    }

    private val _nameError = MutableLiveData(false)
    val nameError: LiveData<Boolean>
        get() = _nameError

    private val _descriptionError = MutableLiveData(false)
    val descriptionError: LiveData<Boolean>
        get() = _descriptionError

    private val _priorityError = MutableLiveData(false)
    val priorityError: LiveData<Boolean>
        get() = _priorityError

    private val _dateError = MutableLiveData(false)
    val dateError: LiveData<Boolean>
        get() = _dateError

    private val _categories: MutableLiveData<List<String>> = MutableLiveData()
    val categories: LiveData<List<String>>
        get() = _categories

    val priorities: LiveData<List<String>>

    init {
        loadCategoriesNames()

        val priorityList = Priority.values()
        priorities = MutableLiveData(priorityList.map { it.getStringValue(app) }.toList())
    }


    var selectedCategoryPosition: Int = if (editMode) {
        val currentPosition = categories.value!!.indexOfFirst {
            it == note!!.category?.name
        }
        if (currentPosition == -1) {
            DEFAULT_CATEGORY_SPINNER_POSITION
        } else {
            currentPosition
        }
    } else {
        DEFAULT_CATEGORY_SPINNER_POSITION
    }

    private var _selectedPriorityPosition = if (editMode) {
        val currentPosition = priorities.value!!.indexOfFirst {
            it == note!!.priority.getStringValue(app)
        }
        currentPosition
    } else {
        DEFAULT_PRIORITY_SPINNER_POSITION
    }

    var selectedPriorityPosition: Int
        get() {
            return _selectedPriorityPosition
        }
        set(value) {
            if (value != _selectedPriorityPosition) {
                _selectedPriorityPosition = value
                resetPriorityError()
            }
        }



    fun resetNameError() {
        _nameError.value = false
    }

    fun resetDescriptionError() {
        _descriptionError.value = false
    }

    fun resetPriorityError() {
        _priorityError.value = false
    }

    fun resetDateError() {
        _dateError.value = false
    }

    fun save(): Boolean {
        if (name.get().toString().isBlank()) _nameError.value = true
        if (description.get().toString().isBlank()) _descriptionError.value = true
        if (date.get().toString().isBlank()) _dateError.value = true
        if (selectedPriorityPosition == 0) _priorityError.value = true

        if (_nameError.value == false &&
            _descriptionError.value == false &&
            _dateError.value == false &&
            _priorityError.value == false
        ) {
            val categories = getAllCategoriesUseCase(authUser)
            val category =
                if (selectedCategoryPosition != 0)
                    categories[selectedCategoryPosition - 1]
                else null

            val priorities = Priority.values()
            val priority = priorities[selectedPriorityPosition]
            if (editMode) {

                editNoteUseCase(
                    note!!.copy(
                        title = name.get().toString(),
                        description = description.get().toString(),
                        completionDate = LocalDate.parse(date.get().toString()),
                        category = category,
                        priority = priority,
                    )
                )
            } else {
                addNoteUseCase(
                    Note(
                        0,
                        name.get().toString(),
                        description.get().toString(),
                        LocalDate.now(),
                        LocalDate.parse(date.get().toString()),
                        false,
                        category,
                        priority,
                        authUser
                    )
                )
            }
            return true
        }
        return false
    }

    fun loadCategoriesNames() {
        val categoriesList = mutableListOf<Category>()
        categoriesList.addAll(getAllCategoriesUseCase(authUser))
        categoriesList.add(0, Category.getNullCategory(app, authUser))
        _categories.value = categoriesList.map { it.name }.toList()
    }

    fun selectCompletionDate() {
        selectDateListener?.selectDate()
    }

    fun addCategory() {
        addCategoryListener?.addCategory()
    }

    interface SelectDateListener {
        fun selectDate()
    }

    interface AddCategoryListener {
        fun addCategory()
    }

    companion object {
        private const val DEFAULT_NAME_VALUE = ""
        private const val DEFAULT_DESCRIPTION_VALUE = ""
        private const val DEFAULT_DATE_VALUE = ""
        private const val DEFAULT_CATEGORY_SPINNER_POSITION = 0
        private const val DEFAULT_PRIORITY_SPINNER_POSITION = 0
    }
}