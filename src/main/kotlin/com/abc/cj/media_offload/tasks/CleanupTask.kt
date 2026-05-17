package com.abc.cj.media_offload.tasks

import com.abc.cj.media_offload.domain.Media
import com.abc.cj.media_offload.domain.MediaType
import java.nio.file.Files
import java.nio.file.Path

class CleanupTask(
    val path: Path,
    val mediaFiles: List<Media>,
) {
    fun cleanup() {
        cleanUpOtherFiles()
        cleanupEmptyDirectories(path)
    }

    private fun cleanUpOtherFiles() {
        for (media in mediaFiles) {
            if(media.mediaType != MediaType.OTHER) continue
            Files.deleteIfExists(media.location)
        }
    }

    private fun cleanupEmptyDirectories(path: Path) {
        Files.walk(path)
            .sorted(Comparator.reverseOrder())
            .filter { it != path && Files.isDirectory(it) }
            .filter { it.toFile().list()?.isEmpty() == true }
            .forEach { Files.delete(it) }
    }
}