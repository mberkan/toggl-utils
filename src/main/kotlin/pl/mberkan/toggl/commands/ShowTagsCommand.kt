package pl.mberkan.toggl.commands

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pl.mberkan.toggl.api.TogglFacade

/**
 * Retrieve all tags list for workspace.
 *
 * @author Marek Berkan
 */
class ShowTagsCommand(private val togglFacade: TogglFacade) {

    companion object { val log: Logger = LoggerFactory.getLogger(ShowTagsCommand::class.java) }

    fun showTags() {
        log.info("retrieveTags begin")
        togglFacade.getTags().forEach {
            println("Tag '${it.name}' -> id: ${it.id}")
        }
        log.info("retrieveTags end")
    }
}
