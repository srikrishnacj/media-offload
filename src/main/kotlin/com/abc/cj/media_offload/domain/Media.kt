package com.abc.cj.media_offload.domain

import com.abc.cj.media_offload.config.props.DeviceConfig
import java.nio.file.Path
import java.time.LocalDateTime

data class Media(
    val mediaType: MediaType,
    val location: Path,
    val name: String,
    val creationData: LocalDateTime,
    val device: DeviceConfig? = null,
)
