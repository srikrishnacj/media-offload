package com.abc.cj.media_offload

import com.abc.cj.media_offload.config.JsonPropertySourceFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources
import org.springframework.shell.core.command.annotation.EnableCommand

@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan
@PropertySources(
    PropertySource(value = ["classpath:device-config.json"], factory = JsonPropertySourceFactory::class),
)
class MediaOffloadApplication

fun main(args: Array<String>) {
    runApplication<MediaOffloadApplication>(*args)
}


