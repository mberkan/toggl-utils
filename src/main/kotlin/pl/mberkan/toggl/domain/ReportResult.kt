package pl.mberkan.toggl.domain

import pl.mberkan.toggl.domain.ReportStatus

data class ReportResult(val reportStatus: ReportStatus, val description: String)
