package com.bykamri.submission.server.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DaoUserFavourite {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userFavourite: UserFavourite)

    @Update
    fun update(userFavourite: UserFavourite)

    @Delete
    fun delete(userFavourite: UserFavourite)

    @Query("SELECT * FROM userfavourite")
    fun getAll(): LiveData<List<UserFavourite>>

    @Query("SELECT * FROM userfavourite WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<UserFavourite>

    @Query("SELECT EXISTS (SELECT 1 FROM userfavourite WHERE username = :username)")
    fun checkFavoriteUser(username: String): LiveData<Boolean>

    @Query("SELECT EXISTS (SELECT 1 FROM  userfavourite WHERE username = :username)")
    fun isInFavorite(username: String): Boolean
}