package com.example.carelyze.data.dto

import com.google.gson.annotations.SerializedName

data class UserRegisterDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("full_name")
    val fullName: String? = null,
)

data class UserLoginDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
)

data class AuthResponseDto(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("user")
    val user: AuthUserDto?,
)

data class AuthUserDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("full_name")
    val fullName: String? = null,
)

data class UserProfileResponseDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("full_name")
    val fullName: String? = null,
)
