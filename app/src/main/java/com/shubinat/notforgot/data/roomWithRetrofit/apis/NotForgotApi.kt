package com.shubinat.notforgot.data.roomWithRetrofit.apis

import com.shubinat.notforgot.data.roomWithRetrofit.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NotForgotApi {
    @POST("users/auth")
    fun authUser(@Body data: LoginRequest): Call<LoginResponse>

    @POST("users/register")
    fun registerUser(@Body data: RegisterRequest): Call<UserModel>

    @GET("categories")
    fun getAllCategories(@Header("Authorization") token: String): List<CategoryModel>

    @POST("categories")
    fun addCategory(@Header("Authorization") token: String, @Body category: CategoryModel): Call<CategoryModel>

    @GET("notes")
    fun getAllNotes(@Header("Authorization") token: String): Call<List<NoteModel>>

    @GET("notes/{id}")
    fun getNote(@Header("Authorization") token: String, @Path("id") id: Int): Call<NoteModel>

    @POST("notes")
    fun addNote(@Header("Authorization") token: String, @Body category: NoteModel): Call<NoteModel>

    @PUT("notes/{id}")
    fun putNote(@Header("Authorization") token: String, @Path("id") id: Int): Call<NoteModel>

    @DELETE("notes/{id}")
    fun deleteNote(@Header("Authorization") token: String, @Path("id") id: Int): Call<NoteModel>
}