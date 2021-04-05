package pl.mberkan.toggl.domain

import pl.mberkan.toggl.AppConfig
import pl.mberkan.toggl.api.TogglFacade
import pl.mberkan.toggl.utils.formatMillisAsHours
import pl.mberkan.toggl.utils.percentage
import java.time.LocalDate

/**
 * Calculate time duration statistics for each tag.
 *
 * @author Marek Berkan
 */
class StatisticsService(private val config: AppConfig, private val togglFacade: TogglFacade) {

    fun calculateReportEntries(dateSince: LocalDate, dateTo: LocalDate, totalDuration: Long): List<ReportEntry> {
        val tags = togglFacade.getTags()

        return tags.map { tag ->
            ReportEntry(
                tag.name,
                getSummaryDurationForTag(dateSince, dateTo, tag.id),
                totalDuration
            )
        }
    }

    private fun getSummaryDurationForTag(dateSince: LocalDate, dateTo: LocalDate, tagId: Int): Long {
        val detailedTimeReportForTag = togglFacade.detailedTimeReportForTag(dateSince, dateTo, tagId)
        return detailedTimeReportForTag.total_grand
    }

    private fun calculateSumForNeededTags(tagsSummary: List<ReportEntry>): Long {
        return tagsSummary.filter { tag -> config.getNeededTags().contains(tag.tagName) }
            .map { tag -> tag.durationMillis }.sum()
    }

    fun createReport(dateSince: LocalDate, dateTo: LocalDate): ReportResult {

        val detailedTimeReportForAllTags = togglFacade.detailedTimeReport(dateSince, dateTo)

        val statisticsService = StatisticsService(config, togglFacade)
        val totalDuration = detailedTimeReportForAllTags.total_grand
        val statsForTags: List<ReportEntry> = statisticsService.calculateReportEntries(dateSince, dateTo, totalDuration)

        val result = StringBuilder()

        result.append(
            """Report:
since:       $dateSince
to:          $dateTo
total_grand: ${formatMillisAsHours(totalDuration)} (h)
total_count: ${detailedTimeReportForAllTags.total_count} (items)

Tags:
"""
        )

        statsForTags
            .sortedWith(compareByDescending { it.durationMillis })
            .forEach { entry -> result
                .append("- ")
                .append(String.format("%-20s", entry.tagName))
                .append("${entry.getDurationAsHours()} (${entry.getDurationAsPercentage()}%)\n") }
        result.append("\n")

        val neededTagsSum = statisticsService.calculateSumForNeededTags(statsForTags)
        val neededTagsPercentage = percentage(neededTagsSum, totalDuration);

        if (neededTagsPercentage != null) {
            if (neededTagsPercentage < config.getNeededTagsThreshold()) {
                result.append("Needed tags sum is too small (${formatMillisAsHours(neededTagsSum)}, ${neededTagsPercentage}%, " +
                        "${config.getNeededTagsThreshold()}% needed) -> focus on important tasks!")
                return ReportResult(ReportStatus.NEEDED_TAGS_SUM_IS_TOO_SMALL, result.toString())
            } else {
                result.append("Needed tags sum is enough (${formatMillisAsHours(neededTagsSum)}, ${neededTagsPercentage}%, " +
                        "${config.getNeededTagsThreshold()}% needed) -> you may do something else if you want")
                return ReportResult(ReportStatus.NEEDED_TAGS_SUM_IS_ENOUGH, result.toString())
            }
        } else {
            result.append("Cannot calculate percentage for neededTagsSum $neededTagsSum and total_grand $totalDuration")
            return ReportResult(ReportStatus.REPORT_ERROR, result.toString())
        }
    }
}
