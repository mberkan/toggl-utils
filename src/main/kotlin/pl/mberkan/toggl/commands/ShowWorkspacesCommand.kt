package pl.mberkan.toggl.commands

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pl.mberkan.toggl.api.TogglFacade

/**
 * Retrieve all workspaces list for account.
 *
 * @author Marek Berkan
 */
class ShowWorkspacesCommand(private val togglFacade: TogglFacade) {

    companion object { val log: Logger = LoggerFactory.getLogger(ShowWorkspacesCommand::class.java) }

    fun showWorkspaces() {
        log.info("retrieveWorkspaces begin")
        togglFacade.getWorkspaces().forEach {
            println("Workspace '${it.name}' -> id: ${it.id}")
        }
        log.info("retrieveWorkspaces end")
    }
}
