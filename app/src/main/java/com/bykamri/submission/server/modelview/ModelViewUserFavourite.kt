package com.bykamri.submission.server.modelview

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bykamri.submission.server.db.RepoUserFavourite
import com.bykamri.submission.server.db.UserFavourite

class ModelViewUserFavourite (application: Application) : ViewModel() {
    private val repository = RepoUserFavourite(application)

    fun insert(userFavourite: UserFavourite) {
        repository.insert(userFavourite)
    }

    fun delete(userFavourite: UserFavourite) {
        repository.delete(userFavourite)
    }

    fun getAll(): LiveData<List<UserFavourite>> = repository.getAll()

    fun checkFavoriteUser(username: String): LiveData<Boolean> = repository.checkFavoriteUser(username)

//    fun getFavoriteUserByUsername(username: String): LiveData<UserFavourite> =
//        repository.getFavoriteUserByUsername(username)
}