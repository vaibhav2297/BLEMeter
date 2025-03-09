package com.example.blemeter.core.file

import com.example.blemeter.config.constants.Storage
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ZipGenerator : IZipGenerator {


    override  fun createZipFile(sourceDir: File, destinationDir: File): File? {
        val zipFile = File(destinationDir, Storage.DebugLogZip)
        val buffer = ByteArray(1024)
        val origin = File(sourceDir.absolutePath)

        try {
            val fos = FileOutputStream(zipFile)
            val zos = ZipOutputStream(BufferedOutputStream(fos))

            writeToZip(origin, "", zos, buffer)

            zos.close()
            fos.close()

            return zipFile
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    private fun writeToZip(source: File, parentPath: String, zos: ZipOutputStream, buffer: ByteArray) {
        val path = if (parentPath.isBlank()) source.name else "$parentPath/${source.name}"

        if (source.isDirectory) {
            val dir = ZipEntry("$path/")
            zos.putNextEntry(dir)
            zos.closeEntry()

            source.listFiles()?.forEach { file ->
                writeToZip(file, path, zos, buffer)
            }
        } else {
            val entry = ZipEntry(path)
            zos.putNextEntry(entry)

            FileInputStream(source).use { fis ->
                var len: Int
                while (fis.read(buffer).also { len = it } > 0) {
                    zos.write(buffer, 0, len)
                }
            }

            zos.closeEntry()
        }
    }
}