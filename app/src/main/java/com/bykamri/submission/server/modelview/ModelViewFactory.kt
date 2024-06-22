package com.bykamri.submission.server.modelview

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bykamri.submission.server.utils.SettingUtils

class ModelViewFactory (
    private val pref: SettingUtils, private val application: Application
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: ModelViewFactory? = null

        @JvmStatic
        fun getInstance(pref: SettingUtils, application: Application): ModelViewFactory {
            if (INSTANCE == null) {
                synchronized(ModelViewFactory::class.java) {
                    INSTANCE = ModelViewFactory(pref, application)
                }
            }
            return INSTANCE as ModelViewFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ModelViewMain::class.java) -> {
                ModelViewMain(pref = pref) as T
            }

            modelClass.isAssignableFrom(ModelViewUserFavourite::class.java) -> {
                ModelViewUserFavourite(application = application) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")

        }
    }
}