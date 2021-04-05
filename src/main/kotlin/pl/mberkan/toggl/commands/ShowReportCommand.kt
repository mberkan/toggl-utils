package pl.mberkan.toggl.commands

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pl.mberkan.toggl.AppConfig
import pl.mberkan.toggl.api.TogglFacade
import pl.mberkan.toggl.domain.StatisticsService
import java.time.LocalDate

/**
 * Retrieve and calculate reports for tags
 *
 * @author Marek Berkan
 */
class ShowReportCommand(private val config: AppConfig, private val togglFacade: TogglFacade) {

    companion object { val log: Logger = LoggerFactory.getLogger(ShowReportCommand::class.java) }

    fun showReport() {
        log.info("showReport begin")

        val dateSince = LocalDate.now().minusDays(config.getReportDurationInDays())
        val dateTo = LocalDate.now()

        val reportResult = StatisticsService(config, togglFacade).createReport(dateSince, dateTo)
        print(reportResult.description)

        log.info("showReport end")
    }
}
