package com.abc.cj.media_offload.domain

import java.nio.file.Path
import java.time.LocalDateTime

data class Media(
    val mediaType: MediaType,
    val location: Path,
    val name: String,
    val creationData: LocalDateTime,
    val deviceModel: String,
)