package com.example.blemeter.feature.scan.di

import com.example.blemeter.core.ble.data.IBLEService
import com.example.blemeter.feature.scan.data.repository.ScanRepository
import com.example.blemeter.feature.scan.domain.repository.IScanRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ScanModule {

    @Provides
    @Singleton
    fun provideScanRepository(
        bleService: IBLEService
    ): IScanRepository =
        ScanRepository(bleService)
}