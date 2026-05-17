package com.abc.cj.media_offload.tasks

import com.abc.cj.media_offload.domain.Media
import com.abc.cj.media_offload.domain.MediaType
import com.abc.cj.media_offload.util.FileExt
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.time.format.DateTimeFormatter
import kotlin.io.path.createDirectories

class MoveTask(
    val path: Path,
    val mediaFiles: List<Media>,
) {

    fun move() {
        var count = 0
        for (media in mediaFiles) {
            if (media.mediaType == MediaType.OTHER) continue
            if (media.mediaType == MediaType.UNKNOWN) continue
            move(media, count++, mediaFiles.size)
        }
    }

    private fun move(media: Media, index: Int, total: Int) {
        val prefix = media.mediaType.prefix().uppercase()
        val date = media.creationData.toLocalDate().toString()
        val time = media.creationData.toLocalTime().format(DateTimeFormatter.ofPattern("HH-mm-ss"))
        val model = media.deviceModel.uppercase()
        val extension = FileExt.extension(media.name)
        val newName = "${prefix}_${date}_${time}_${model}.${extension}"
        val newPath = path
            .resolve("SORTED_DST")
            .resolve(media.creationData.toLocalDate().toString())
            .resolve(media.mediaType.prefix().uppercase())
            .resolve(newName)

        println("[$index/$total] Moving ${media.name} to: $newPath \t from: ${media.location}")
        newPath.parent.createDirectories()
        Files.move(media.location, newPath, StandardCopyOption.REPLACE_EXISTING);
    }
}