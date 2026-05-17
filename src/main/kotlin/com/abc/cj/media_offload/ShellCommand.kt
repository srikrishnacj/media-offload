package com.abc.cj.media_offload

import org.springframework.shell.core.command.annotation.Command
import org.springframework.shell.core.command.annotation.Option
import org.springframework.stereotype.Component
import java.nio.file.Path

@Component
class ShellCommand {
    @Command(
        name = ["offload"],
        description = "rename media files and organize it by date",
        group = "Media Offload"
    )
    fun offload(
        @Option(
            shortName = 'p',
            longName = "path",
            description = "media location",
            required = true,
        ) path: String
    ) {
        val workflow = Workflow(
            dryRun = false,
            path = Path.of(path),
        )
        workflow.run()
    }
}
