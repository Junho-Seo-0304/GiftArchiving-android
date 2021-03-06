package com.nexters.giftarchiving.model

import com.google.gson.annotations.SerializedName

internal data class User(
    @SerializedName("token")
    val userId: String,
    @SerializedName("loginType")
    val loginType: String = "KAKAO"
)