package com.nexters.giftarchiving.service.network

import com.nexters.giftarchiving.model.LoginResponse
import com.nexters.giftarchiving.model.User
import com.nexters.giftarchiving.model.WriteResponse
import okhttp3.MultipartBody
import retrofit2.http.*

internal interface GiftZipService {
    @POST("/api/user/signIn")
    suspend fun signIn(
        @Body user: User
    ): LoginResponse

    @Multipart
    @POST("/api/gift/create")
    suspend fun createGift(
        @Query("createdBy") createdBy: String,
        @Query("isReceiveGift") isReceiveGift: Boolean,
        @Query("name") name: String,
        @Query("content") content: String,
        @Query("receiveDate") receiveDate: String,
        @Query("category") category: String,
        @Query("reason") reason: String,
        @Query("emotion") emotion: String,
        @Query("bgColor") bgColor: String,
        @Part noBgImg: MultipartBody.Part,
        @Part bgImg: MultipartBody.Part
    ): WriteResponse

    @Multipart
    @POST("/api/gift/testImg")
    suspend fun testImg(
        @Part img: MultipartBody.Part
    ): String

}