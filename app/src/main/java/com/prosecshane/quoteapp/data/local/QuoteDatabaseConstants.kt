package com.prosecshane.quoteapp.data.local

/**
 * Singleton object that holds constants for the local database.
 */
object QuoteDatabaseConstants {
    /**
     * Name of the database file.
     */
    const val filename = "quotes.db"

    /**
     * Name and current version of the table that holds saved quotes.
     */
    const val tableName = "saved_quotes"
    const val tableVersion = 1
}
