package com.example.blemeter.core.file.di

import android.content.Context
import com.example.blemeter.core.file.FileService
import com.example.blemeter.core.file.IFileService
import com.example.blemeter.core.file.IZipGenerator
import com.example.blemeter.core.file.ZipGenerator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FileModule {

    @Provides
    @Singleton
    fun bindFileService(
        @ApplicationContext context: Context,
        zipGenerator: IZipGenerator
    ): IFileService =
        FileService(context,zipGenerator)

    @Provides
    @Singleton
    fun bindZipGenerator(): IZipGenerator =
        ZipGenerator()
}