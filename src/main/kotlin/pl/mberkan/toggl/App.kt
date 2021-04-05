package pl.mberkan.toggl

import pl.mberkan.toggl.api.TogglFacade
import pl.mberkan.toggl.api.TogglFacadeImpl
import pl.mberkan.toggl.commands.SendReportCommand
import pl.mberkan.toggl.commands.ShowReportCommand
import pl.mberkan.toggl.commands.ShowTagsCommand
import pl.mberkan.toggl.commands.ShowWorkspacesCommand
import pl.mberkan.toggl.utils.MailUtil
import kotlin.system.exitProcess

class App(private val config: AppConfig) {

    fun getTogglService(): TogglFacade {
        return TogglFacadeImpl(config)
    }

    fun getMailUtil(): MailUtil {
        return MailUtil(config)
    }
}

fun main(args: Array<String>) {

    if (args.isEmpty()) {
        println("""Execution commands: 
            show-workspaces 
            show-tags
            show-report
            send-report""")
        exitProcess(1)
    }

    val config = AppConfigImpl()
    config.loadConfig()
    val app = App(config)
    when(args[0]) {
        "show-workspaces" -> ShowWorkspacesCommand(app.getTogglService()).showWorkspaces()
        "show-tags" -> ShowTagsCommand(app.getTogglService()).showTags()
        "show-report" -> ShowReportCommand(config, app.getTogglService()).showReport()
        "send-report" -> SendReportCommand(config, app.getTogglService(), app.getMailUtil()).sendReport()
        else -> {
            println("Unexpected command: " + args[0])
            exitProcess(2)
        }
    }
}


