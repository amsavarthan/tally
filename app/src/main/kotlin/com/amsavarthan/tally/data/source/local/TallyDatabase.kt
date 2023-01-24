package com.amsavarthan.tally.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.amsavarthan.tally.data.utils.TallyTypeConverters
import com.amsavarthan.tally.domain.entity.Account
import com.amsavarthan.tally.domain.entity.Category
import com.amsavarthan.tally.domain.entity.Transaction

@Database(
    entities = [
        Transaction::class,
        Account::class,
        Category::class
    ],
    version = 1,
)
@TypeConverters(TallyTypeConverters::class)
abstract class TallyDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun accountDao(): AccountDao

    companion object {
        const val DB_NAME: String = "tally_database"
    }

}