package com.bykamri.submission.server.db

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class UserFavourite (
    @PrimaryKey
    @ColumnInfo(name = "username")
    var username: String = "no username",

    @ColumnInfo(name = "name")
    var name: String = "no name",

    @ColumnInfo(name = "picture_url")
    var pictureUrl: String? = null,

    @ColumnInfo(name = "type")
    var type: String = "User"
) : Parcelable
