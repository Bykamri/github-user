package com.bykamri.submission.server.model

import com.google.gson.annotations.SerializedName

data class FollowUsers (
    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("html_url")
    val htmlUrl: String,

    @field:SerializedName("type")
    val type: String,
)