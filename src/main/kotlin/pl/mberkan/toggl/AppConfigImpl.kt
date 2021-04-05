package pl.mberkan.toggl

import java.io.FileInputStream
import java.util.*

/**
 * Config from file or System.getProperty
 *
 * @author Marek Berkan
 */
class AppConfigImpl : AppConfig {

    val propertiesFileName = ".toggl-utils.properties"
    val prop = Properties()

    override fun loadConfig() {
        val userHome = System.getProperty("user.home")
        val fis = FileInputStream("$userHome/$propertiesFileName")
        prop.load(fis)
    }

    override fun getProperty(name: String, defaultValue: String?): String {
        if (System.getProperty(name) != null)  {
            return System.getProperty(name)
        }
        if (prop.getProperty(name) != null) {
            return prop.getProperty(name)
        }
        if (defaultValue == null) {
            throw IllegalArgumentException("No $name property found")
        }
        return defaultValue
    }

    override fun getTogglApiToken(): String = getProperty("togglApiToken", null)

    override fun getWorkspaceId(): Int = getProperty("workspaceId", null).toInt()

    override fun getNeededTags(): Set<String> = getProperty("neededTags", "Kodowanie,CR")
        .split(",")
        .toSet()

    override fun getNeededTagsThreshold(): Int = getProperty("neededTagsThreshold", "55").toInt()

    override fun getReportDurationInDays(): Long = getProperty("reportDurationInDays", "7").toLong()

    override fun getUserAgent(): String = getProperty("userAgent", null)

    override fun getSmtpHost(): String = getProperty("smtpHost", null)

    override fun getMailFrom(): String = getProperty("mailFrom", null)

    override fun getMailTo(): List<String> = getProperty("mailTo", null).split(",")
}
