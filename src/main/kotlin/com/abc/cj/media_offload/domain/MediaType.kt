package com.abc.cj.media_offload.domain

import com.abc.cj.media_offload.util.FileExt

enum class MediaType {
    IMAGE, VIDEO, OTHER, UNKNOWN;

    fun prefix(): String {
        return when (this) {
            IMAGE -> "img"
            VIDEO -> "vid"
            OTHER -> "other"
            UNKNOWN -> "UNKNOWN"
        }
    }

    companion object {
        private val imageExtensions = listOf("arw", "heic", "jpg", "jpeg", "png", "gif")
        private val videoExtensions = listOf("mp4")
        private val otherExtensions = listOf("lrv", "thm")

        fun type(fileName: String): MediaType {
            val ext = FileExt.extension(fileName)
            return when {
                imageExtensions.contains(ext) -> IMAGE
                videoExtensions.contains(ext) -> VIDEO
                otherExtensions.contains(ext) -> OTHER
                else -> UNKNOWN
            }
        }
    }
}