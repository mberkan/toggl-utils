package pl.mberkan.toggl.commands

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pl.mberkan.toggl.AppConfig
import pl.mberkan.toggl.api.TogglFacade
import pl.mberkan.toggl.domain.ReportResult
import pl.mberkan.toggl.domain.ReportStatus
import pl.mberkan.toggl.domain.StatisticsService
import pl.mberkan.toggl.utils.MailUtil
import java.time.LocalDate

/**
 * Retrieve, calculate and send report for tags.
 *
 * @author Marek Berkan
 */
class SendReportCommand(private val config: AppConfig, private val togglFacade: TogglFacade, private val mailUtil: MailUtil) {

    companion object { val log: Logger = LoggerFactory.getLogger(SendReportCommand::class.java) }

    fun sendReport() {
        log.info("sendReport begin")

        val dateSince = LocalDate.now().minusDays(config.getReportDurationInDays())
        val dateTo = LocalDate.now()

        val reportResult: ReportResult = StatisticsService(config, togglFacade).createReport(dateSince, dateTo)

        val subject: String = when(reportResult.reportStatus) {
            ReportStatus.NEEDED_TAGS_SUM_IS_ENOUGH -> "Toggl-utils report since $dateSince -> needed tags sum is enough"
            ReportStatus.NEEDED_TAGS_SUM_IS_TOO_SMALL -> "Toggl-utils report since $dateSince -> needed tags sum is too small"
            ReportStatus.REPORT_ERROR -> "Toggl-utils report since $dateSince -> error"
        }

        mailUtil.sendEmail(config.getMailTo(), subject, reportResult.description)

        log.info("sendReport end")
    }
}
