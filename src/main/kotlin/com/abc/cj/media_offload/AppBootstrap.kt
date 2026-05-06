package com.abc.cj.media_offload

import com.abc.cj.media_offload.config.props.Configurations
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import kotlin.system.exitProcess

@Component
class AppBootstrap(
    private val configurations: Configurations,
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        val workflow = Workflow(
            dryRun = true,
            configurations = configurations
        )

        workflow.run()
        exitProcess(0)
    }
}