package com.bykamri.submission.server.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserFavourite::class], version = 1, exportSchema = false)
abstract class RdUserFavourite  : RoomDatabase() {
    abstract fun favoriteUserDao(): DaoUserFavourite

    companion object {
        @Volatile
        private var INSTANCE: RdUserFavourite? = null

        @JvmStatic
        fun getDatabase(context: Context): RdUserFavourite {
            if (INSTANCE == null) {
                synchronized(RdUserFavourite::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        RdUserFavourite::class.java, "note_database"
                    )
                        .build()
                }
            }
            return INSTANCE as RdUserFavourite
        }
    }

}