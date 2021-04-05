package pl.mberkan.toggl.domain

import pl.mberkan.toggl.utils.formatMillisAsHours
import pl.mberkan.toggl.utils.percentage

/**
 * Work duration statistics for tag.
 *
 * @author Marek Berkan
 */
data class ReportEntry(val tagName: String, val durationMillis: Long, private val totalDuration: Long) {

    fun getDurationAsHours(): String? = formatMillisAsHours(durationMillis)

    fun getDurationAsPercentage(): Long? = percentage(durationMillis, totalDuration)
}
