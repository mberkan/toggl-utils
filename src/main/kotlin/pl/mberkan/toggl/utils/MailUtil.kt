package pl.mberkan.toggl.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pl.mberkan.toggl.AppConfig
import java.net.InetAddress
import java.util.*
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class MailUtil(private val appConfig: AppConfig) {
    companion object {
        val log: Logger = LoggerFactory.getLogger(MailUtil::class.java)
    }

    fun sendEmail(toEmail: List<String>, subject: String, body: String) {

        val userName = System.getProperty("user.name")
        val localMachine: InetAddress = InetAddress.getLocalHost()
        val bodyWithFooter = body + """

--
Sent from $userName@$localMachine
"""

        try {
            val msg = MimeMessage(getSession())
            //set message headers
            msg.addHeader("Content-type", "text/plain; charset=UTF-8")
            msg.addHeader("format", "flowed")
            msg.addHeader("Content-Transfer-Encoding", "8bit")
            msg.setFrom(InternetAddress(appConfig.getMailFrom()))
            msg.setSubject(subject, "UTF-8")
            msg.setText(bodyWithFooter, "UTF-8")
            msg.sentDate = Date()
            msg.setRecipients(Message.RecipientType.TO, toInternetAddressArray(toEmail))
            log.debug("Message to '$toEmail' with subject '$subject' is ready")
            Transport.send(msg)
            log.info("Email sent successfully!!")
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun getSession(): Session? {
        val props = System.getProperties()
        props["mail.smtp.host"] = appConfig.getSmtpHost()
        return Session.getInstance(props, null)
    }

    private fun toInternetAddressArray(addressesAsStrings: List<String>): Array<InternetAddress> {
        return addressesAsStrings.map { InternetAddress(it, false) }.toTypedArray()
    }
}
