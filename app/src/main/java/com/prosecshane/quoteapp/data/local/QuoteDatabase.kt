package com.prosecshane.quoteapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.prosecshane.quoteapp.domain.model.Quote

/**
 * An abstract class that will be implemented by Room as a Database object.
 * Provides a Dao.
 */
@Database(
    version = QuoteDatabaseConstants.tableVersion,
    entities = [Quote::class]
)
abstract class QuoteDatabase : RoomDatabase() {
    /**
     * An abstract function that provides a Dao.
     *
     * @return A Dao to work with this database.
     */
    abstract fun quoteDao(): QuoteDao
}
