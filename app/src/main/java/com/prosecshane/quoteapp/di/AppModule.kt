package com.prosecshane.quoteapp.di

import com.prosecshane.quoteapp.data.remote.QuoteApi
import com.prosecshane.quoteapp.data.remote.QuoteApiConstants
import com.prosecshane.quoteapp.utils.Cryptography
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * DaggerHilt module that provides singletons for the app.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /**
     * A "provide" function that creates an instance of an interface.
     *
     * @return Instance of the [QuoteApi] interface.
     */
    @Provides
    @Singleton
    fun provideQuoteApi(): QuoteApi {
        val token = Cryptography.decryptCaesar(QuoteApiConstants.EncryptedBearerToken)
        val okHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
                chain.proceed(chain.request().newBuilder()
                    .header("Authorization", "Bearer $token").build()
                )
            }.build()
        return Retrofit.Builder()
            .baseUrl(QuoteApiConstants.BaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(QuoteApi::class.java)
    }
}
