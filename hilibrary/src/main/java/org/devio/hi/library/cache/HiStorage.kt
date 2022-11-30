package org.devio.hi.library.cache

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * @Author yanyonghua
 * @Date 2022/11/15-11:32
 * @Des $.
 */
object HiStorage {
    fun <T> saveCache(key: String, body: T) {
        val cache = Cache()
        cache.key = key
        cache.data = toByteArray(body)
        CacheDatabase.get().cacheDao.saveCache(cache)
    }

    fun <T> getCache(key: String):T? {
        val cache = CacheDatabase.get().cacheDao.getCache(key)
        return (if (cache?.data!=null){
            toObject(cache?.data)
        }else null)as? T
    }

    fun deleteCache(key: String){
        val cache=Cache()
        cache.key =key
        CacheDatabase.get().cacheDao.deleteCache(cache)
    }
    private fun <T> toByteArray(body: T): ByteArray? {
        var boas: ByteArrayOutputStream? = null
        var oos: ObjectOutputStream? = null
        try {
            boas = ByteArrayOutputStream()
            oos = ObjectOutputStream(boas)
            oos.writeObject(body)
            oos.flush()
            return boas.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            boas?.close()
            oos?.close()
        }
        return ByteArray(0)
    }

    private fun toObject(data: ByteArray?): Any? {
        var bais: ByteArrayInputStream? = null
        var ois: ObjectInputStream? = null

        try {
            bais = ByteArrayInputStream(data)
            ois = ObjectInputStream(bais)
            return ois.readObject()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            bais?.close()
            ois?.close()
        }

        return null
    }
}