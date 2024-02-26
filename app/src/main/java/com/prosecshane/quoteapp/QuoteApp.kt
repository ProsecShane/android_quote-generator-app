package com.prosecshane.quoteapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Custom application context, used with DaggerHilt.
 */
@HiltAndroidApp
class QuoteApp: Application()
