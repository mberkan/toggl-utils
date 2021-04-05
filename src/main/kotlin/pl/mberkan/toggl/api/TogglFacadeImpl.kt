package pl.mberkan.toggl.api

import feign.Feign
import feign.gson.GsonDecoder
import feign.slf4j.Slf4jLogger
import pl.mberkan.toggl.AppConfig
import java.time.LocalDate
import java.util.*

/**
 * Service to retrieve data from Toggl
 *
 * @author Marek Berkan
 */
class TogglFacadeImpl(private val config: AppConfig) : TogglFacade {

    private val authorizationHeader = "Basic " +
            Base64.getEncoder().encodeToString(("${config.getTogglApiToken()}:api_token").toByteArray())

    private val togglApiUrl = "https://api.track.toggl.com"
    private val togglClient: TogglClient = Feign.builder()
        .decoder(GsonDecoder())
        .logger(Slf4jLogger())
        .logLevel(feign.Logger.Level.FULL)
        .target(TogglClient::class.java, togglApiUrl)

    override fun getWorkspaces(): List<Workspace> = togglClient.getWorkspaces(authorizationHeader)

    override fun getTags(): List<Tag> = togglClient.getTags(authorizationHeader, config.getWorkspaceId())

    override fun detailedTimeReport(dateSince: LocalDate, dateTo: LocalDate) = togglClient.detailedTimeReport(
        authorizationHeader, config.getWorkspaceId(), dateSince, dateTo, config.getUserAgent())

    override fun detailedTimeReportForTag(dateSince: LocalDate, dateTo: LocalDate, tagId: Int) = togglClient.detailedTimeReportForTag(
        authorizationHeader, config.getWorkspaceId(), dateSince, dateTo, config.getUserAgent(), tagId)
}
