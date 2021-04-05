package pl.mberkan.toggl.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import pl.mberkan.toggl.AppConfig
import pl.mberkan.toggl.api.DetailedReport
import pl.mberkan.toggl.api.Tag
import pl.mberkan.toggl.api.TogglFacade
import java.time.LocalDate

internal class StatisticsServiceTest {

    private lateinit var appConfig : AppConfig
    private lateinit var togglFacade : TogglFacade
    private lateinit var statisticsService : StatisticsService

    @BeforeEach
    fun beforeAll() {
        appConfig = mock()
        togglFacade = mock()
        statisticsService = StatisticsService(appConfig, togglFacade)
    }

    @Test
    fun `should calculate for 2 different tags`() {
        // given
        val dateSince = LocalDate.of(2021, 3, 1)
        val dateTo = LocalDate.of(2021, 3, 8)
        val totalDuration: Long = 1000*8*16

        whenever(appConfig.getNeededTags()).thenReturn(setOf("Kodowanie"))
        `when`(togglFacade.getTags()).thenReturn(listOf(
            Tag(1, "Kodowanie"),
            Tag(2, "Spotkania")
        ))
        `when`(togglFacade.detailedTimeReportForTag(dateSince, dateTo, 1)).thenReturn(DetailedReport(1000*60*8, 1))
        `when`(togglFacade.detailedTimeReportForTag(dateSince, dateTo, 2)).thenReturn(DetailedReport(1000*60*8, 1))

        // when
        val result = statisticsService.calculateReportEntries(dateSince, dateTo, totalDuration)

        // then
        assertThat(result).hasSize(2)
        assertThat(result).contains(ReportEntry("Kodowanie", 1000*60*8, 1000*8*16))
        assertThat(result).contains(ReportEntry("Spotkania", 1000*60*8, 1000*8*16))
    }

    @Test
    fun `should create raport for 2 different tags and warn`() {

        // given
        val dateSince = LocalDate.of(2021, 3, 1)
        val dateTo = LocalDate.of(2021, 3, 8)

        whenever(appConfig.getNeededTags()).thenReturn(setOf("Kodowanie"))
        whenever(appConfig.getNeededTagsThreshold()).thenReturn(51)
        `when`(togglFacade.detailedTimeReport(dateSince, dateTo)).thenReturn(DetailedReport(1000*60*60*8*2, 2))
        `when`(togglFacade.getTags()).thenReturn(listOf(
            Tag(1, "Kodowanie"),
            Tag(2, "Spotkania")
        ))
        `when`(togglFacade.detailedTimeReportForTag(dateSince, dateTo, 1)).thenReturn(DetailedReport(1000*60*60*8, 1))
        `when`(togglFacade.detailedTimeReportForTag(dateSince, dateTo, 2)).thenReturn(DetailedReport(1000*60*60*8, 1))


        // when
        val result = statisticsService.createReport(dateSince, dateTo)

        // then
        assertThat(result.reportStatus).isEqualTo(ReportStatus.NEEDED_TAGS_SUM_IS_TOO_SMALL)
        assertThat(result.description).isEqualTo("""Report:
since:       2021-03-01
to:          2021-03-08
total_grand: 16:00 (h)
total_count: 2 (items)

Tags:
- Kodowanie           8:00 (50%)
- Spotkania           8:00 (50%)

Needed tags sum is too small (8:00, 50%, 51% needed) -> focus on important tasks!
""".trimIndent())
    }

    @Test
    fun `should create raport for 2 different tags and confirm`() {

        // given
        val dateSince = LocalDate.of(2021, 3, 1)
        val dateTo = LocalDate.of(2021, 3, 8)

        whenever(appConfig.getNeededTags()).thenReturn(setOf("Kodowanie"))
        whenever(appConfig.getNeededTagsThreshold()).thenReturn(51)
        `when`(togglFacade.detailedTimeReport(dateSince, dateTo)).thenReturn(DetailedReport(1000*60*60*8*2, 2))
        `when`(togglFacade.getTags()).thenReturn(listOf(
            Tag(1, "Kodowanie"),
            Tag(2, "Spotkania")
        ))
        `when`(togglFacade.detailedTimeReportForTag(dateSince, dateTo, 1)).thenReturn(DetailedReport(1000*60*60*10, 1))
        `when`(togglFacade.detailedTimeReportForTag(dateSince, dateTo, 2)).thenReturn(DetailedReport(1000*60*60*6, 1))


        // when
        val result = statisticsService.createReport(dateSince, dateTo)

        // then
        assertThat(result.reportStatus).isEqualTo(ReportStatus.NEEDED_TAGS_SUM_IS_ENOUGH)
        assertThat(result.description).isEqualTo("""Report:
since:       2021-03-01
to:          2021-03-08
total_grand: 16:00 (h)
total_count: 2 (items)

Tags:
- Kodowanie           10:00 (62%)
- Spotkania           6:00 (37%)

Needed tags sum is enough (10:00, 62%, 51% needed) -> you may do something else if you want
""".trimIndent())
    }

    @Test
    fun `should create raport for 3 different tags and confirm`() {

        // given
        val dateSince = LocalDate.of(2021, 3, 1)
        val dateTo = LocalDate.of(2021, 3, 8)

        whenever(appConfig.getNeededTags()).thenReturn(setOf("Kodowanie", "CR"))
        whenever(appConfig.getNeededTagsThreshold()).thenReturn(51)
        `when`(togglFacade.detailedTimeReport(dateSince, dateTo)).thenReturn(DetailedReport(1000*60*60*8*2, 2))
        `when`(togglFacade.getTags()).thenReturn(listOf(
            Tag(1, "Kodowanie"),
            Tag(2, "Spotkania"),
            Tag(3, "CR")
        ))
        `when`(togglFacade.detailedTimeReportForTag(dateSince, dateTo, 1)).thenReturn(DetailedReport(1000*60*60*6, 1))
        `when`(togglFacade.detailedTimeReportForTag(dateSince, dateTo, 2)).thenReturn(DetailedReport(1000*60*60*6, 1))
        `when`(togglFacade.detailedTimeReportForTag(dateSince, dateTo, 3)).thenReturn(DetailedReport(1000*60*60*4, 1))


        // when
        val result = statisticsService.createReport(dateSince, dateTo)

        // then
        assertThat(result.reportStatus).isEqualTo(ReportStatus.NEEDED_TAGS_SUM_IS_ENOUGH)
        assertThat(result.description).isEqualTo("""Report:
since:       2021-03-01
to:          2021-03-08
total_grand: 16:00 (h)
total_count: 2 (items)

Tags:
- Kodowanie           6:00 (37%)
- Spotkania           6:00 (37%)
- CR                  4:00 (25%)

Needed tags sum is enough (10:00, 62%, 51% needed) -> you may do something else if you want
""".trimIndent())
    }
}
