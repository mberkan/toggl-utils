package pl.mberkan.toggl.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class FormatterUtilKtTest {
    @Test
    fun should_format_one_sec() {
        // when
        val result = formatMillisAsHours(1000)
        // then
        assertThat(result).isEqualTo("0:00")
    }

    @Test
    fun should_format_one_minute() {
        // when
        val result = formatMillisAsHours(1000*60)
        // then
        assertThat(result).isEqualTo("0:01")
    }

    @Test
    fun should_format_one_hour() {
        // when
        val result = formatMillisAsHours(1000*60*60)
        // then
        assertThat(result).isEqualTo("1:00")
    }

    @Test
    fun should_format_one_hour_and_one_minute() {
        // when
        val result = formatMillisAsHours(1000*60*60 + 1000*60)
        // then
        assertThat(result).isEqualTo("1:01")
    }

    @Test
    fun should_format_25_hours() {
        // when
        val result = formatMillisAsHours(1000*60*60*25)
        // then
        assertThat(result).isEqualTo("25:00")
    }
}
