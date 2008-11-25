
import org.apache.log4j.Logger;

import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.ByteArrayResource

import org.springframework.mail.MailException
import org.springframework.mail.MailSender
import org.springframework.mail.javamail.MimeMessageHelper

import javax.mail.internet.MimeMessage
import javax.mail.internet.InternetAddress;

import org.springframework.context.ApplicationContextAware
import org.springframework.context.ApplicationContext

/**
* Service for sending emails.
*/
class EMailAuthenticatedService implements ApplicationContextAware {

   boolean transactional = false
   MailSender mailSender
   //EMailProperties eMailProperties
   def ApplicationContext applicationContext

   public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext
	}

   def sendEmail = { mail, attachements ->
       EMailProperties eMailProperties = applicationContext.getBean("eMailProperties")

       MimeMessage mimeMessage = mailSender.createMimeMessage()    

        // start creation of mime message
       MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "ISO-8859-1")
       helper.from = new InternetAddress(eMailProperties.from)
       // helper got confused to swithed to setter.
       helper.setTo(getInternetAddresses(mail.to))
       helper.subject = mail.subject
       helper.setText(mail.text, true);
       if(mail.bcc) helper.setBcc(getInternetAddresses(mail.bcc));
       if(mail.cc) helper.setCc(getInternetAddresses(mail.cc));

       // add any attachments
       attachements.each { key, value ->
           helper.addAttachment(key, new ByteArrayResource(value))
       }

       mailSender.send mimeMessage
   }

   private InternetAddress[] getInternetAddresses(List emails) {
       InternetAddress[] mailAddresses = new InternetAddress[emails.size()];
	   emails.eachWithIndex {mail, i ->
	   		mailAddresses[i] = new InternetAddress(mail)
	   }
       return mailAddresses;
   }
}