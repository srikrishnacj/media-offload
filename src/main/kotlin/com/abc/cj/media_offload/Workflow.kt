package com.abc.cj.media_offload

import com.abc.cj.media_offload.config.props.Configurations
import com.abc.cj.media_offload.tasks.MediaFinderTask
import java.nio.file.Path

class Workflow(
    private val dryRun: Boolean,
    private val configurations: Configurations,
){
    fun run() {
        scanAllFiles()
    }

    private fun scanAllFiles(){
        val dir = "/Volumes/xD/TEMP"

        val task = MediaFinderTask(Path.of(dir))
        task.find()
    }
}