class FirstJob {
	def timeout = 5000l     // execute job once in 5 seconds

    def execute() {
	    // execute task
	    return
	    println "Hello from FirstJob: "+ new Date()
	}
}
