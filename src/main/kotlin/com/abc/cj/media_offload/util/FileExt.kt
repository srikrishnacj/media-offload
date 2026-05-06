package com.abc.cj.media_offload.util

import java.nio.file.Path

object FileExt {
    fun extension(path: Path): String? {
        if (!path.fileName.toString().contains(".")) return null
        return path.fileName.toString().substringAfterLast(".").lowercase()
    }

    fun extension(fileName: String): String? {
        if (!fileName.contains(".")) return null
        return fileName.substringAfterLast(".").lowercase()
    }
}