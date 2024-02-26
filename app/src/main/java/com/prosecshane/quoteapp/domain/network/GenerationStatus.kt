package com.prosecshane.quoteapp.domain.network

/**
 * Sealed class to indicate the status that the quote generation is in.
 * Contains four different statuses.
 */
sealed class GenerationStatus {
    /**
     * Data object that indicates that no quote generation is taking place.
     */
    data object None: GenerationStatus()

    /**
     * Data object that indicates that the quote is being generated at the moment.
     */
    data object InProgress: GenerationStatus()

    /**
     * Data class that indicates that an error has occurred during the quote's generation.
     *
     * @param message Error message describing the error that has occurred.
     */
    data class Error(val message: String): GenerationStatus()

    /**
     * Data class that indicates that the quote was generated successfully.
     *
     * @param quote The generated quote.
     */
    data class Success(val quote: String): GenerationStatus()
}
