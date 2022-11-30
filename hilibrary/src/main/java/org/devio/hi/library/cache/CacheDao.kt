package org.devio.hi.library.cache

import androidx.room.*

/**
 * @Author yanyonghua
 * @Date 2022/11/15-11:27
 * @Des $.
 */
@Dao
interface CacheDao {

    @Insert(entity = Cache::class,onConflict = OnConflictStrategy.REPLACE)
    fun saveCache(cache: Cache):Long

    @Query("select * from cache where `key`=:key" )
    fun getCache(key:String):Cache?

    @Delete(entity = Cache::class)
    fun deleteCache(cache: Cache)
}