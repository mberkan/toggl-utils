package pl.mberkan.toggl

interface AppConfig {
    fun loadConfig()
    fun getProperty(name: String, defaultValue: String?): String
    fun getTogglApiToken(): String
    fun getWorkspaceId(): Int
    fun getNeededTags(): Set<String>
    fun getNeededTagsThreshold(): Int
    fun getReportDurationInDays(): Long
    fun getUserAgent(): String
    fun getSmtpHost(): String
    fun getMailFrom(): String
    fun getMailTo(): List<String>
}
