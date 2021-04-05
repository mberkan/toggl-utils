package pl.mberkan.toggl.api

import java.time.LocalDate

interface TogglFacade {
    fun getWorkspaces(): List<Workspace>
    fun getTags(): List<Tag>
    fun detailedTimeReport(dateSince: LocalDate, dateTo: LocalDate): DetailedReport
    fun detailedTimeReportForTag(dateSince: LocalDate, dateTo: LocalDate, tagId: Int): DetailedReport
}
