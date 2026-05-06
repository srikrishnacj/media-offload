package com.abc.cj.media_offload.tasks

import com.abc.cj.media_offload.util.FileExt
import com.abc.cj.media_offload.batteries.ConsoleTable
import com.abc.cj.media_offload.domain.Media
import com.abc.cj.media_offload.domain.MediaType
import java.nio.file.Path

class MediaFinderTask(
    val path: Path,
) {
    fun find(): List<Media> {
        val files = findAllFiles(path)
        val types = mediaTypes(files)
        print(types)
        return listOf()
    }

    fun findAllFiles(path: Path): List<Path> {
        return path.toFile().walkTopDown()
            .filter { it.isFile }
            .map { it.toPath() }
            .toList()
    }

    fun mediaTypes(files: List<Path>): Map<Path, MediaType> {
        return files.associateWith { MediaType.type(it.fileName.toString()) }
    }

    private fun print(types: Map<Path, MediaType>) {
        val map = mutableMapOf<MediaType, Int>()
        for ((key, value) in types) {
            val count = map.getOrDefault(value, 0)
            map[value] = count + 1
        }

        val console = ConsoleTable()
        console.setHeader("Type", "Count")
        console.addRow("Total", types.size.toString())
        for ((key, value) in map) {
            console.addRow("$key", "$value")
        }
        console.printTable()

        if (map.contains(MediaType.UNKNOWN)) {
            val list = mutableSetOf<String>()
            for ((key, value) in types) {
                if(value != MediaType.UNKNOWN) continue
                val ext = FileExt.extension(key)
                if (ext != null) list.add(ext)
            }
            println("Unknown Extensions: $list")
        }
    }
}