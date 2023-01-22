package com.shubinat.notforgot.data.roomWithRetrofit.workers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.GsonBuilder
import com.shubinat.notforgot.R
import com.shubinat.notforgot.data.roomWithRetrofit.database.AppDatabase
import com.shubinat.notforgot.data.roomWithRetrofit.exceptions.BadRequestException
import com.shubinat.notforgot.data.roomWithRetrofit.exceptions.NotFoundException
import com.shubinat.notforgot.data.roomWithRetrofit.exceptions.UnauthorizedException
import com.shubinat.notforgot.data.roomWithRetrofit.models.CategoryModel
import com.shubinat.notforgot.data.roomWithRetrofit.models.NoteModel
import com.shubinat.notforgot.data.roomWithRetrofit.repository.UserRepositoryImpl
import com.shubinat.notforgot.data.roomWithRetrofit.services.NetworkService
import com.shubinat.notforgot.domain.entity.LoginUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class AddCategoryWorker(
    private val context: Context,
    private val workerParameters: WorkerParameters
) :
    Worker(context, workerParameters) {

    private val repository = UserRepositoryImpl(context)
    private val api = NetworkService.getInstance().getNotForgotApi()
    private val db = AppDatabase.getInstance(context)
    private val model by lazy {
        val json = inputData.getString(AddNoteWorker.NOTE_KEY)
        val gson = GsonBuilder().create()
        return@lazy gson.fromJson(json, CategoryModel::class.java)
    }
    private val user by lazy {
        val userId = inputData.getInt(AddNoteWorker.USER_ID_KEY, 0)
        return@lazy db.userDao().findUser(userId)
            ?: throw RuntimeException("User not found: $userId")
    }

    override fun doWork(): Result {
        showNotification()
        try {
            val dbUser = db.userDao().findUser(user.id) ?: throw UnauthorizedException()
            val token = "Bearer " + dbUser.token
            val result = api.addCategory(token, model).execute()
            when (result.code()) {
                400 -> throw BadRequestException()
                404 -> throw NotFoundException()
                401 -> throw UnauthorizedException()
                else -> {
                    return Result.success()
                }
            }

        } catch (ex: UnauthorizedException) {
            runBlocking {
                val loginUser = LoginUser(user.login, user.password)
                repository.authorizeUser(loginUser)
            }
            return Result.retry()
        } catch (ex: Exception) {
            return Result.failure()
        } finally {
            cancelNotification()
        }
    }

    private fun showNotification() {
        val notification = Notification.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_adaptive_fore)
            .setContentTitle(context.getString(R.string.notification_sync))
            .setProgress(1, 0, true)
            .setColor(ContextCompat.getColor(context, R.color.purple_200))

        val notificationChannel =
            NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
            }
        val notificationManager =
            context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, notification.build())
        }
    }

    private fun cancelNotification() {
        with(NotificationManagerCompat.from(context)) {
            cancel(NOTIFICATION_ID)

        }
    }

    companion object {
        private const val CHANNEL_ID = "not-forgot-channel-id"
        private const val CHANNEL_NAME = "not-forgot-channel-name"
        private const val CHANNEL_DESCRIPTION = "not-forgot-channel-DESCRIPTION"

        private const val NOTIFICATION_ID = 101

        const val USER_ID_KEY = "USER_ID"
        const val CATEGORY_KEY = "CATEGORY_DATA"
    }
}