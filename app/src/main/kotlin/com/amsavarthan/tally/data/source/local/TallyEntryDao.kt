package com.amsavarthan.tally.data.source.local

import androidx.room.*
import com.amsavarthan.tally.domain.entity.TallyEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface TallyEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(tallyEntry: TallyEntry)

    @Delete
    suspend fun deleteEntry(tallyEntry: TallyEntry)

    @Query("SELECT * FROM entry ORDER BY datetime(date_time) DESC")
    fun getEntries(): Flow<List<TallyEntry>>

    @Query("SELECT MAX(COALESCE(SUM(CASE WHEN category_type = 'expense' THEN amount ELSE -amount END), 0),0) FROM entry WHERE strftime('%W',date_time,'utc') = strftime('%W','now','utc')")
    fun getAmountSpentInCurrentWeek(): Flow<Double>

    @Query("SELECT * FROM entry WHERE strftime('%W',date_time,'utc') = strftime('%W','now','utc') ORDER BY datetime(date_time) DESC")
    fun getEntriesOfCurrentWeek(): Flow<List<TallyEntry>>

}