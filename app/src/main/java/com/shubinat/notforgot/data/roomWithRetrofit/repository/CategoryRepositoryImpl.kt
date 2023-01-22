package com.shubinat.notforgot.data.roomWithRetrofit.repository

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.*
import com.google.gson.GsonBuilder
import com.shubinat.notforgot.data.roomWithRetrofit.database.AppDatabase
import com.shubinat.notforgot.data.roomWithRetrofit.exceptions.UnauthorizedException
import com.shubinat.notforgot.data.roomWithRetrofit.models.CategoryModel
import com.shubinat.notforgot.data.roomWithRetrofit.models.NoteModel
import com.shubinat.notforgot.data.roomWithRetrofit.services.NetworkService
import com.shubinat.notforgot.data.roomWithRetrofit.utils.convertToSuspend
import com.shubinat.notforgot.data.roomWithRetrofit.workers.AddCategoryWorker
import com.shubinat.notforgot.data.roomWithRetrofit.workers.EditNoteWorker
import com.shubinat.notforgot.domain.entity.*
import com.shubinat.notforgot.domain.repository.CategoryRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CategoryRepositoryImpl private constructor(private val context: Context) : CategoryRepository {
    private val db = AppDatabase.getInstance(context)
    private val service = NetworkService.getInstance()
    private val api = service.getNotForgotApi()
    private val userRepository = UserRepositoryImpl.getInstance(context)
    private var firstLoad = true

    override suspend fun addCategory(category: Category) {
        if (category.name.isBlank()) throw RuntimeException("Name cannot be blank")
        if (getCategories(category.user).firstOrNull { it.name == category.name } != null)
            throw RuntimeException("Category with name: ${category.name} is exist")

        db.categoryDao().addCategory(
            com.shubinat.notforgot.data.roomWithRetrofit.entities.Category(
                category.id,
                category.name,
                category.user.id
            )
        )
        coroutineScope {
            launch {
                addCategoryBackground(category)
            }
        }
    }

    override suspend fun getCategories(user: User): List<Category> {
        var categories: List<Category>
        if (service.isOnline(context) && firstLoad) {
            try {
                val dbUser = db.userDao().findUser(user.id) ?: throw UnauthorizedException()
                val token = "Bearer " + dbUser.token
                categories = loadCategoriesFromApi(token, user)
            } catch (ex: UnauthorizedException) {
                val loginUser = LoginUser(user.login, user.password)
                runBlocking {
                    userRepository.authorizeUser(loginUser)
                }
                categories = getCategories(user)
            }
            firstLoad = false
        } else {
            categories = loadCategoriesFromDb(user)
        }
        return categories
    }

    override suspend fun getCategory(id: Int): Category {
        val category =
            db.categoryDao().getCategory(id) ?: throw RuntimeException("Category not found: $id")
        val user =
            db.userDao().findUser(category.userId) ?: throw RuntimeException("User not found $id")
        return Category(
            category.id,
            category.name,
            User(user.id, user.name, user.login, user.password)
        )
    }

    private suspend fun loadCategoriesFromApi(token: String, user: User): List<Category> {
        val categoryTask = coroutineScope {
            async {
                api.getAllCategories(token).convertToSuspend()
            }
        }
        val categoryModels = categoryTask.await()
        val categories = categoryModels.map {
            Category(
                it.id,
                it.name,
                user
            )
        }
        saveCategoriesInDb(categories)
        return categories
    }

    private fun loadCategoriesFromDb(user: User): List<Category> {
        return db.categoryDao().getAll(user.id).map {
            Category(
                it.id,
                it.name,
                user
            )
        }
    }

    private fun saveCategoriesInDb(categories: List<Category>) {
        db.categoryDao().clear()
        for (category in categories) {
            db.categoryDao()
                .addCategory(
                    com.shubinat.notforgot.data.roomWithRetrofit.entities.Category(
                        category.id,
                        category.name,
                        category.user.id
                    )
                )
        }
    }

    private fun addCategoryBackground(category: Category) {
        val gson = GsonBuilder().create()
        val model = CategoryModel(
            category.id,
            category.name,
        )
        val json = gson.toJson(model)

        val data = Data.Builder()
            .putString(AddCategoryWorker.CATEGORY_KEY, json)
            .putInt(AddCategoryWorker.USER_ID_KEY, category.user.id)
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<AddCategoryWorker>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        val manager = WorkManager.getInstance(context)
        manager.enqueue(workRequest)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: CategoryRepositoryImpl? = null

        fun getInstance(context: Context): CategoryRepositoryImpl {
            if (INSTANCE == null)
                INSTANCE = CategoryRepositoryImpl(context)
            return INSTANCE as CategoryRepositoryImpl
        }
    }
}