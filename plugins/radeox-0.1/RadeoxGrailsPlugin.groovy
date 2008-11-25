
class RadeoxGrailsPlugin {
	def version = 0.1
	def dependsOn = [:]
	def title = 'A plugin for the Radeox Wiki-render engine to allow easy markup and cross-linking.'
	def author = 'Dierk Koenig'
	def authorEmail = 'dierk.koenig@canoo.com'
	def description = '''\
The Radeox plugin provides a new tag <g:radeoxRender> some content </g:radeoxRender> that 
processes the inlined content according to the Radeox Wiki Format, including directives like
__bold__ and ~~italic~~ but also more advanced macros like {table} and {list-of-macros}.
You can also define your own macros in Groovy.
Radeox is easy to extend and customize. The plugin comes with an inital implementation of 
cross liking to pages that are marked up like [pageName] in the content. this will link
to the PageController show action.''' 
	def documentation = 'http://grails.org/Radeox+plugin'
	
	
	def doWithSpring = {
		// TODO Implement runtime spring config (optional)
	}   
	def doWithApplicationContext = { applicationContext ->
		// TODO Implement post initialization spring config (optional)		
	}
	def doWithWebDescriptor = { xml ->
		// TODO Implement additions to web.xml (optional)
	}	                                      
	def doWithDynamicMethods = { ctx ->
		// TODO Implement additions to web.xml (optional)
	}	
	def onChange = { event ->
		// TODO Implement code that is executed when this class plugin class is changed  
		// the event contains: event.application and event.applicationContext objects
	}                                                                                  
	def onApplicationChange = { event ->
		// TODO Implement code that is executed when any class in a GrailsApplication changes
		// the event contain: event.source, event.application and event.applicationContext objects
	}
}
