package com.abc.cj.media_offload

import com.abc.cj.media_offload.tasks.CleanupTask
import com.abc.cj.media_offload.tasks.MediaFinderTask
import com.abc.cj.media_offload.tasks.MoveTask
import java.nio.file.Path

class Workflow(
    private val dryRun: Boolean,
    private val path: Path,
){
    fun run() {
        val task = MediaFinderTask(path = path)
        val mediaFiles = task.find()

        val moveTask = MoveTask(path = path, mediaFiles = mediaFiles)
        moveTask.move()

        val cleanupTask = CleanupTask(path = path, mediaFiles = mediaFiles)
        cleanupTask.cleanup()
    }
}