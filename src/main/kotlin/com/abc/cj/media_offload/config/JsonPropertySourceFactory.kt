@file:Suppress("UNCHECKED_CAST")

package com.abc.cj.media_offload.config

import org.springframework.core.env.MapPropertySource
import org.springframework.core.env.PropertySource
import org.springframework.core.io.support.EncodedResource
import org.springframework.core.io.support.PropertySourceFactory
import tools.jackson.databind.ObjectMapper

class JsonPropertySourceFactory : PropertySourceFactory {
    private val objectMapper = ObjectMapper()

    override fun createPropertySource(name: String?, resource: EncodedResource): PropertySource<*> {
        val map = objectMapper.readValue(resource.inputStream, Map::class.java)
        val flatMap = flatten(map as Map<String, Any>)
        return MapPropertySource(
            name ?: resource.resource.filename ?: "jsonPropertySource",
            flatMap
        )
    }

    private fun flatten(map: Map<String, Any>, prefix: String = ""): Map<String, Any> {
        val result = mutableMapOf<String, Any>()
        for ((key, value) in map) {
            val fullKey = if (prefix.isEmpty()) key else "$prefix.$key"
            when (value) {
                is Map<*, *> -> result.putAll(flatten(value as Map<String, Any>, fullKey))
                is List<*>   -> result.putAll(flattenList(value, fullKey))
                else         -> result[fullKey] = value
            }
        }
        return result
    }

    private fun flattenList(list: List<*>, prefix: String): Map<String, Any> {
        val result = mutableMapOf<String, Any>()
        list.forEachIndexed { index, item ->
            when (item) {
                is Map<*, *> -> result.putAll(flatten(item as Map<String, Any>, "$prefix[$index]"))
                else         -> result["$prefix[$index]"] = item ?: ""
            }
        }
        return result
    }
}