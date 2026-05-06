package com.abc.cj.media_offload.domain

import java.time.LocalDateTime

data class ExifData(
    val make: String,
    val model: String,
    val creationDate: LocalDateTime,
)
