
import groovy.text.Template
import groovy.text.SimpleTemplateEngine

import org.springframework.context.ApplicationContextAware
import org.springframework.context.ApplicationContext
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.mail.MailException
import org.springframework.core.io.ByteArrayResource

class BatchService implements ApplicationContextAware {
    boolean transactional = false
	
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext
	}
	
	def ApplicationContext applicationContext
	def EMailAuthenticatedService eMailAuthenticatedService
	
	ReportService reportService  // Inject ReportService
	
	/*
	*  Runs nightly reports
	*/
	def nightlyReports = {
			log.info "Running Nightly Batch Job: "+new Date()
	
			// 1. Gather User w/ Email Addresses
			// This should work but results in NullPointerError
			// Workaround, Use criteria
            // def users = User.findAllByEmailIsNotNull()
			
			def users = User.withCriteria {
               isNotNull('email')
		  	}
    
			users?.each { user ->
			    // 2. Invoke Report Service for each User
			    //    Can't Reuse ReportController, it makes too 
			    //    many assumptions. such as, access to session.class
			    //
			    //    Reuse Report Service and pass appropriate params
			    // Gather the data to be reported
			    def inputCollection = Todo.findAllByOwner(user)
			    Map params = new HashMap()
			    params.inputCollection = inputCollection
			    params.userName = user.firstName+" "+user.lastName
			    
			    // Load the report file
			    def reportFile = this.class.getClassLoader().getResource("web-app/reports/userTodo.jasper")
			    ByteArrayOutputStream byteArray = reportService.generateReport(reportFile, reportService.PDF_FORMAT,params )
			    
			    
			    Map attachments = new HashMap()
			    attachments.put("TodoReport.pdf", byteArray.toByteArray())
			
			    // 3. Email results to the user
			    sendNotificationEmail(user, attachments)
			
			}		
			log.info "Completed Nightly Batch Job:  "+new Date()
	}
	
	def private sendNotificationEmail = {User user, Map attachments ->
		def emailTpl = this.class.getClassLoader().getResource( 
				"web-app/WEB-INF/nightlyReportsEmail.gtpl")
		def binding = ["user": user]
		def engine = new SimpleTemplateEngine()
		def template = engine.createTemplate(emailTpl).make(binding)
		def body = template.toString()

		def email = [
            to: [user.email],
			subject: "Your Collab-Todo Report",
			text: 	body
		]
		try {
			EMailProperties eMailProperties = applicationContext.getBean("eMailProperties")
            // email properties is now looked up inside the service.
            // eMailAuthenticatedService.sendEmail(email, eMailProperties, attachments)
            eMailAuthenticatedService.sendEmail(email, attachments)
        } catch (MailException ex) {
			log.error("Failed to send emails", ex)
			return false
		}
		return true
	}
}