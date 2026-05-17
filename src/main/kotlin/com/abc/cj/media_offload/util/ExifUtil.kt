package com.abc.cj.media_offload.util

import com.abc.cj.media_offload.domain.ExifData
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val EXIF_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")

object ExifUtil {
    fun read(path: Path): ExifData {
        val tags = readAllTags(path)
        val model = readCameraModel(tags)
        val creationDate = readCreateDate(tags)
            .let { runCatching { LocalDateTime.parse(it.take(19), EXIF_DATE_FORMAT) }.getOrNull() }
            ?: LocalDateTime.MIN
        return ExifData(model = model, creationDate = creationDate)
    }

    private fun readAllTags(path: Path): Map<String, String> {
        return ExecUtil.exec("exiftool \"$path\"")
            .mapNotNull { line ->
                val idx = line.indexOf(':')
                if (idx < 0) null
                else line.substring(0, idx).trim() to line.substring(idx + 1).trim()
            }
            .toMap()
    }

    fun readCameraModel(tags: Map<String, String>): String {
        return tags["Camera Model Name"] ?:
        tags["Model"] ?:
        tags["Android Model"] ?:
        ""
    }

    fun readCreateDate(tags: Map<String, String>): String {
        return tags["Date/Time Original"] ?: tags["Create Date"] ?: tags["Modify Date"] ?: ""
    }
}