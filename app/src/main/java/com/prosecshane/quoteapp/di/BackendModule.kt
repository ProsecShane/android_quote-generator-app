package com.prosecshane.quoteapp.di

import com.prosecshane.quoteapp.data.remote.QuoteClientImpl
import com.prosecshane.quoteapp.data.repository.QuoteRepositoryImpl
import com.prosecshane.quoteapp.data.sharedpreferences.SPApiImpl
import com.prosecshane.quoteapp.domain.repository.QuoteClient
import com.prosecshane.quoteapp.domain.repository.QuoteRepository
import com.prosecshane.quoteapp.domain.sharedpreferences.SPApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * DaggerHilt module that binds singletons used in backend call operations.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class BackendModule {
    /**
     * Function that creates (binds) a repository singleton.
     *
     * @param quoteRepositoryImpl Implementation of an interface in form of a
     * [QuoteRepositoryImpl] class instance.
     * @return Instance of the [QuoteRepository] interface.
     */
    @Binds
    @Singleton
    abstract fun bindQuoteRepository(
        quoteRepositoryImpl: QuoteRepositoryImpl
    ): QuoteRepository

    /**
     * Function that creates (binds) a client singleton.
     *
     * @param quoteClient Implementation of an interface in form of a
     * [QuoteClientImpl] class instance.
     * @return Instance of the [QuoteClient] interface.
     */
    @Binds
    @Singleton
    abstract fun bindQuoteClient(
        quoteClient: QuoteClientImpl
    ): QuoteClient

    /**
     * Function that creates (binds) a SPApi singleton.
     *
     * @param spApi Implementation of an interface in form of a [SPApiImpl] class instance.
     * @return Instance of the [SPApi] interface.
     */
    @Binds
    @Singleton
    abstract fun bindSPApi(
        spApi: SPApiImpl,
    ): SPApi
}
