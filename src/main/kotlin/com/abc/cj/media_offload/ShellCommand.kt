package com.abc.cj.media_offload

import com.abc.cj.media_offload.config.props.Configurations
import org.springframework.shell.core.command.annotation.Command
import org.springframework.shell.core.command.annotation.Option
import org.springframework.stereotype.Component
import java.nio.file.Path

@Component
class ShellCommand(
    private val configurations: Configurations,
) {
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
            configurations = configurations,
            path = Path.of(path),
        )
        workflow.run()
    }
}
