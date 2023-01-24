package com.amsavarthan.tally.domain.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Entity(
    tableName = "category",
    indices = [
        Index(
            value = ["name", "type"],
            unique = true
        )
    ],
)
@Serializable
@Parcelize
data class Category(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    val id: Long? = null,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "emoji")
    val emoji: String,
    @ColumnInfo(name = "type")
    val type: CategoryType,
) : Parcelable