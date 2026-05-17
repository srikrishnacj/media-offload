package com.abc.cj.media_offload.util

import java.util.concurrent.TimeUnit

object ExecUtil {
    fun exec(cmd: String, timeoutSeconds: Long = 5): List<String> {
        val process = ProcessBuilder("sh", "-c", cmd)
            .redirectErrorStream(true)
            .start()

        val completed = process.waitFor(timeoutSeconds, TimeUnit.SECONDS)
        if (!completed) {
            process.destroyForcibly().waitFor()
            return emptyList()
        }
        return process.inputStream.bufferedReader().readLines()
    }
}