package com.bykamri.submission.server.db

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class RepoUserFavourite (application: Application) {
    private val mDaoUserFavourite: DaoUserFavourite
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = RdUserFavourite.getDatabase(application)
        mDaoUserFavourite = db.favoriteUserDao()
    }

    fun insert(favoriteUser: UserFavourite) {
        executorService.execute { mDaoUserFavourite.insert(favoriteUser) }
    }

    fun delete(favoriteUser: UserFavourite) {
        executorService.execute { mDaoUserFavourite.delete(favoriteUser) }
    }

    fun getAll(): LiveData<List<UserFavourite>> {
        return mDaoUserFavourite.getAll()
    }

    fun getFavoriteUserByUsername(username: String): LiveData<UserFavourite> {
        return mDaoUserFavourite.getFavoriteUserByUsername(username)
    }

    fun checkFavoriteUser(username: String): LiveData<Boolean> {
        return mDaoUserFavourite.checkFavoriteUser(username)
    }
}