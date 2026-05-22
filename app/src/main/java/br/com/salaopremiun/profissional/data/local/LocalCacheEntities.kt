package br.com.salaopremiun.profissional.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Upsert

@Entity(tableName = "cached_items")
data class CachedItemEntity(
    @PrimaryKey val cacheKey: String,
    val payloadJson: String,
    val updatedAtMillis: Long,
)

@Dao
interface LocalCacheDao {
    @Query("SELECT * FROM cached_items WHERE cacheKey = :key LIMIT 1")
    suspend fun get(key: String): CachedItemEntity?

    @Upsert
    suspend fun upsert(item: CachedItemEntity)

    @Query("DELETE FROM cached_items WHERE cacheKey = :key")
    suspend fun delete(key: String)

    @Query("DELETE FROM cached_items")
    suspend fun clear()
}

@Database(
    entities = [CachedItemEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class ProfessionalDatabase : RoomDatabase() {
    abstract fun cacheDao(): LocalCacheDao

    companion object {
        @Volatile
        private var instance: ProfessionalDatabase? = null

        fun get(context: Context): ProfessionalDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    ProfessionalDatabase::class.java,
                    "salaopremiun-profissional.db",
                ).build().also { instance = it }
            }
        }
    }
}
