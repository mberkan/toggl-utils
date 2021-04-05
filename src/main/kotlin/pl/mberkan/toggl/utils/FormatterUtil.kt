package pl.mberkan.toggl.utils

import java.time.Duration

fun formatMillisAsHours(value: Long?): String? {
    if (value == null) {
        return null
    }
    val duration = Duration.ofMillis(value)
    return "" + duration.toHours() + ":" + String.format("%02d", duration.toMinutesPart())
}
