class NightlyReportsJob {
	def cronExpression = "0 0 1 * * ?"

	def name = "Nightly"       // Job Name
	def group = "CollabTodo"   // Job Group
	
	// def startDelay = 20000     // Wait 20 Sec to start the job
	// def timeout = 30000        // execute job once every 60 seconds
	
    def batchService
	
    def execute() {
		log.info "Starting Nightly Reports Job: "+ new Date()
	    batchService.nightlyReports.call()
	    log.info "Finished Nightly Reports Job: "+ new Date()
	}
}
