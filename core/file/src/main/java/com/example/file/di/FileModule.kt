package com.example.file.di

import android.content.Context
import com.example.file.FileService
import com.example.file.IFileService
import com.example.file.IZipGenerator
import com.example.file.ZipGenerator
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