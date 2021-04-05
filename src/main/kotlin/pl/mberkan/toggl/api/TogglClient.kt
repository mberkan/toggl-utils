package pl.mberkan.toggl.api

import feign.Headers
import feign.Param
import feign.RequestLine
import java.time.LocalDate

/**
 * API client to toggl.com
 */
interface TogglClient {

    @Headers("Authorization: {authorizationHeader}")
    @RequestLine("GET /api/v8/workspaces")
    fun getWorkspaces(
        @Param("authorizationHeader") authorizationHeader: String,
    ): List<Workspace>

    @Headers("Authorization: {authorizationHeader}")
    @RequestLine("GET /api/v8/workspaces/{workspaceId}/tags")
    fun getTags(
        @Param("authorizationHeader") authorizationHeader: String,
        @Param("workspaceId") workspaceId: Int): List<Tag>

    @Headers("Authorization: {authorizationHeader}")
    @RequestLine("GET /reports/api/v2/details?workspace_id={workspaceId}&since={since}&until={until}&user_agent={userAgent}")
    fun detailedTimeReport(
        @Param("authorizationHeader") authorizationHeader: String,
        @Param("workspaceId") workspaceId: Int,
        @Param("since") since: LocalDate,
        @Param("until") until: LocalDate,
        @Param("userAgent") userAgent: String
    ): DetailedReport

    @Headers("Authorization: {authorizationHeader}")
    @RequestLine("GET /reports/api/v2/details?workspace_id={workspaceId}&since={since}&until={until}&user_agent={userAgent}&tag_ids={tagIds}")
    fun detailedTimeReportForTag(
        @Param("authorizationHeader") authorizationHeader: String,
        @Param("workspaceId") workspaceId: Int,
        @Param("since") since: LocalDate,
        @Param("until") until: LocalDate,
        @Param("userAgent") userAgent: String,
        @Param("tagIds") tagId: Int
    ): DetailedReport
}
