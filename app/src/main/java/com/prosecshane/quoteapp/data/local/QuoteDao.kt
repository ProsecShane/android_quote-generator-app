package com.prosecshane.quoteapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prosecshane.quoteapp.domain.model.Quote

/**
 * Dao for the local database. Enables to read and write to the tables of the database.
 * This interface will be implemented automatically by Room.
 */
@Dao
interface QuoteDao {
    /**
     * An INSERT function that adds a quote to the table.
     *
     * @param quote A quote to add.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addQuote(quote: Quote)

    /**
     * A function that deletes a certain quote by it's ID.
     *
     * @param id ID of the quote that needs to be deleted.
     */
    @Query("DELETE FROM ${QuoteDatabaseConstants.tableName} WHERE id = :id")
    suspend fun removeQuote(id: String)

    /**
     * A function that deletes all the quotes.
     */
    @Query("DELETE FROM ${QuoteDatabaseConstants.tableName}")
    suspend fun clearQuotes()

    /**
     * A function that gets all the quotes from the table.
     *
     * @return A list of all quotes stored in the local database.
     */
    @Query("SELECT * FROM ${QuoteDatabaseConstants.tableName}")
    suspend fun getQuotes(): List<Quote>
}
