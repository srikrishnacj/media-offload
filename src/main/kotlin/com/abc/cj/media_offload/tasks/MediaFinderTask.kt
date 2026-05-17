package com.abc.cj.media_offload.tasks

import com.abc.cj.media_offload.util.FileExt
import com.abc.cj.media_offload.batteries.ConsoleTable
import com.abc.cj.media_offload.domain.Media
import com.abc.cj.media_offload.domain.MediaType
import com.abc.cj.media_offload.util.ExifUtil
import com.abc.cj.media_offload.util.PathUtil
import java.nio.file.Path
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator

class MediaFinderTask(
    val path: Path
) {
    fun find(): List<Media> {
        val files = findAllFiles(path)
        println("Found ${files.size} files")

        val pathTypeMapping = mediaTypes(files)
        val media = mutableListOf<Media>()

        println("converting to media files")
        pathTypeMapping.keys
            .filter { key -> pathTypeMapping[key] != MediaType.UNKNOWN }
            .parallelStream()
            .map { key -> mediaFile(key) }
            .forEach { mediaFile -> media.add(mediaFile) }

        println("Media Detection Report")
        printMediaTypes(pathTypeMapping)
        printCameraTypes(media)
        printDateRange(media)

        return media
    }

    private fun findAllFiles(path: Path): List<Path> {
        println("Searching for $path")
        return path.toFile()
            .walkTopDown()
            .filter { it.isFile }
            .filter { !it.isHidden }
            .map { it.toPath() }
            .filter { !it.toAbsolutePath().toString().contains("SORTED_DST") }
            .toList()
    }

    private fun mediaTypes(files: List<Path>): Map<Path, MediaType> {
        return files.associateWith { MediaType.type(it.fileName.toString()) }
    }

    private fun mediaFile(path: Path): Media {
        val mediaType = MediaType.type(path.fileName.toString())
        val exif = ExifUtil.read(path)
        if (exif.creationDate == LocalDateTime.MIN && MediaType.OTHER != mediaType) {
            println("WARNING: Creation Data not available for $path")
        }
        val deviceModel = deviceModel(path) ?: "UNK"
        val media = Media(
            mediaType = mediaType,
            location = path,
            name = path.fileName.toString(),
            creationData = exif.creationDate,
            deviceModel = deviceModel,
        )
        return media
    }

    private fun deviceModel(path: Path): String? {
        val baseLocation = this.path.toAbsolutePath().toString().removeSuffix("/")
        val mediaLocation = path.toAbsolutePath().toString().removeSuffix("/")
        val subPath = mediaLocation.replace(baseLocation, "")
        return PathUtil.topParent(Path.of(subPath))
    }

    private fun printMediaTypes(types: Map<Path, MediaType>) {
        val map = mutableMapOf<MediaType, Int>()
        for ((key, value) in types) {
            val count = map.getOrDefault(value, 0)
            map[value] = count + 1
        }

        val console = ConsoleTable()
        console.setHeader("Type", "Count")
        console.addRow("Total", types.size.toString())
        console.addRow(MediaType.IMAGE.toString(), map.getOrDefault(MediaType.IMAGE, 0).toString())
        console.addRow(MediaType.VIDEO.toString(), map.getOrDefault(MediaType.VIDEO, 0).toString())
        console.addRow(MediaType.OTHER.toString(), map.getOrDefault(MediaType.OTHER, 0).toString())
        console.addRow(MediaType.UNKNOWN.toString(), map.getOrDefault(MediaType.UNKNOWN, 0).toString())
        console.printTable()

        if (map.contains(MediaType.UNKNOWN)) {
            val list = mutableSetOf<String>()
            for ((key, value) in types) {
                if (value != MediaType.UNKNOWN) continue
                val ext = FileExt.extension(key)
                if (ext != null) list.add(ext)
            }
            println("Unknown Extensions: $list")
        }
        println()
    }

    private fun printCameraTypes(mediaList: List<Media>) {
        val map = mutableMapOf<String, Int>()
        for (media in mediaList) {
            val count = map.getOrDefault(media.deviceModel, 0)
            map[media.deviceModel] = count + 1
        }

        val console = ConsoleTable()
        console.setHeader("Device Model", "Count")
        for ((key, value) in map) {
            console.addRow(key, value.toString())
        }
        console.printTable()
    }

    private fun printDateRange(mediaList: List<Media>) {
        val map = mutableMapOf<LocalDate, Int>()
        for (media in mediaList) {
            val count = map.getOrDefault(media.creationData.toLocalDate(), 0)
            map[media.creationData.toLocalDate()] = count + 1
        }

        val console = ConsoleTable()
        console.setHeader("Date", "Count")
        for (date in map.keys.sorted()) {
            console.addRow(date.toString(), map[date]!!.toString())
        }
        console.printTable()
    }
}