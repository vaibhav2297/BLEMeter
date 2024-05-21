package com.example.blemeter.core.file

import java.io.File

interface IZipGenerator {

    fun createZipFile(sourceDir: File, destinationDir: File) : File?
}