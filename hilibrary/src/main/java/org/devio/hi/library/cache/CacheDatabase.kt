package org.devio.hi.library.cache

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.devio.hi.library.util.AppGlobals

/**
 * @Author yanyonghua
 * @Date 2022/11/15-11:22
 * @Des $.
 */
@Database(entities = [Cache::class], version = 1, exportSchema = true)
abstract class CacheDatabase:RoomDatabase() {

    companion object{
        private var database:CacheDatabase

        fun get():CacheDatabase{
            return database
        }
        init {
            val context = AppGlobals.get()!!.applicationContext
            database =Room.databaseBuilder(context,CacheDatabase::class.java,"howow_cache").build()
        }
    }
    abstract val cacheDao:CacheDao
}