package com.abc.cj.media_offload.util

import java.nio.file.Path

object PathUtil {
    fun topParent(path: Path): String? {
        val str = path.toString().removePrefix("/")
        val slashIdx = str.indexOf('/')
        return if (slashIdx < 0) null else str.substring(0, slashIdx)
    }
}