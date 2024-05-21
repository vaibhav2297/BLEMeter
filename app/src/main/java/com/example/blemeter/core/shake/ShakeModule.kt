package com.example.blemeter.core.shake

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ShakeModule {

    @Provides
    @Singleton
    fun bindLogger(
        @ApplicationContext context: Context
    ): ShakeDetector =
        ShakeDetector(context)
}