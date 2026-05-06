package com.abc.cj.media_offload.config.props

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan


@ConfigurationProperties(prefix = "configurations")
data class Configurations(
    val deviceConfigs: List<DeviceConfig>,
)

data class DeviceConfig(
    val make: String,
    val model: String,
    val shortName: String,
)