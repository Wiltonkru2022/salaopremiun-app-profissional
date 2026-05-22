package br.com.salaopremiun.profissional.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_items")
data class CachedItemEntity(
    @PrimaryKey val cacheKey: String,
    val payloadJson: String,
    val updatedAtMillis: Long,
)
