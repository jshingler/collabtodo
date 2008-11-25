/**
 * Gant script that copies a war file to an application
 * server deployment directory.
 */

Ant.property(environment:"env")                             
grailsHome = Ant.antProject.properties."env.GRAILS_HOME"    

includeTargets << new File ( "${grailsHome}/scripts/War.groovy" ) 

target ('default':'''Copies a WAR archive to a Java EE application server's deploy directory.

Example: 
grails deploy
grails prod deploy
''') {
  deploy()
} 

target (deploy: "The implementation target") {
  depends( war )
	 
  def deployDir = Ant.antProject.properties.'deploy.dir'

  Ant.copy(todir:"${deployDir}", overwrite:true) {
    fileset(dir:"${basedir}", includes:"*.war") 
  }       

  event("StatusFinal", ["Done copying WAR to ${deployDir}"])
}