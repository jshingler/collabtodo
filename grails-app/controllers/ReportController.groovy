
import org.springframework.context.ApplicationContext;
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes

class ReportController {
	ReportService reportService

	def index = {   		
        // Gather data for the Report.
        // 1) Find the controller
		ApplicationContext ctx = (ApplicationContext) session.
		     getServletContext().
		     getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT);
		def controller = ctx.getBean("${params._controller}");
		
		// 2) Invoke the action
		def inputCollection = controller."${params._action}"(params)
		params.inputCollection = inputCollection

		// Find the compiled Report
		def reportFileName = reportService.reportFileName("${params.file}")
   		def reportFile = servletContext.getResource(reportFileName)
   		if(reportFile == null){
   		  throw new FileNotFoundException("\"${reportFileName}\" file must be in reports repository.")
   		}
		
		// Call the Report Service to invoke the reporting engine
      	switch(params.format){
      	  case "PDF":
	      	createPdfFile(reportService.generateReport(reportFile,
	      			reportService.PDF_FORMAT,params ).toByteArray(),params.file)
      	  	break
      	  case "HTML":
        	render(text:reportService.generateReport(reportFile,
        			reportService.HTML_FORMAT,params),contentType:"text/html")
          	break
      	  case "CSV":
	        render(text:reportService.generateReport(reportFile,
	        		reportService.CSV_FORMAT,params),contentType:"text")
          	break
      	  case "XLS":
      		createXlsFile(reportService.generateReport(reportFile,
      				reportService.XLS_FORMAT,params).toByteArray(),params.file)
          	break
      	  case "RTF":
      		createRtfFile(reportService.generateReport(reportFile,
      				reportService.RTF_FORMAT,params).toByteArray(),params.file)
          	break
      	  case "XML":
        	render(text:reportService.generateReport(reportFile,
        			reportService.XML_FORMAT,params),contentType:"text")
          	break
      	  case "TXT":
        	render(text:reportService.generateReport(reportFile,
        			reportService.TEXT_FORMAT,params),contentType:"text")
          	break
          default:
            throw new Exception("Invalid format")
          	break
      	}
	}
	
	def list = {}
	def todo2 = {}
  
	/**
	* Output a PDF response
	*/
  	def createPdfFile = { contentBinary, fileName ->
  		response.setHeader("Content-disposition", "attachment; filename=" +
  	    fileName + ".pdf");
    	response.contentType = "application/pdf"
    	response.outputStream << contentBinary
  	}
	
	/**
	* Output an excel response
	*/
  	def createXlsFile = { contentBinary, fileName ->
		response.setHeader("Content-disposition", "attachment; filename=" +
	    fileName + ".xls");
	    response.contentType = "application/vnd.ms-excel"
	    response.outputStream << contentBinary
	}
	
	/**
	* Output an RTF response
	*/
	def createRtfFile = { contentBinary, fileName ->
	    response.setHeader("Content-disposition", "attachment; filename=" +
        fileName + ".rtf");
        response.contentType = "application/rtf"
        response.outputStream << contentBinary
}
  
}