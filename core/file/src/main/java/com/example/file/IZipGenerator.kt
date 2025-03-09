package com.example.file

import java.io.File

interface IZipGenerator {

    fun createZipFile(sourceDir: File, destinationDir: File) : File?
}