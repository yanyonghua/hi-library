package org.devio.hi.library.cache

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @Author yanyonghua
 * @Date 2022/11/15-11:13
 * @Des $.
 */
@Entity(tableName = "cache")
class Cache {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    var key:String =""

    var data :ByteArray?=null
}