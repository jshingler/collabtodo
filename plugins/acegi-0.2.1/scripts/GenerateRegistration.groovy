/*
 * Copyright 2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 
 * @auther Haotian Sun
 * @auther Tsuyoshi Yamamoto
 */
 
import org.codehaus.groovy.grails.commons.GrailsClassUtils as GCU

grailsHome = Ant.project.properties."environment.GRAILS_HOME"

includeTargets << new File ( "${grailsHome}/scripts/Init.groovy" )  


personDomainClassName="Person"
authorityDomainClassName="Authority"
requestmapDomainClassName="Requestmap"

pluginTemplatePath="${acegiPluginDir}/src/templates/manager"

overwrite = false

target('default': "generate user registration views and controllers") {
  depends( configureProxy )
  
  ClassLoader parent = getClass().getClassLoader()
  GroovyClassLoader loader = new GroovyClassLoader(parent)
  Class clazz = loader.parseClass(new File("${basedir}/grails-app/conf/AcegiConfig.groovy"))
  def acegiConfig = new ConfigSlurper().parse(clazz)
  println "Login user domain class: ${acegiConfig.acegi.loginUserDomainClass}"
  println "Authority domain class: ${acegiConfig.acegi.authorityDomainClass}"
  personDomainClassName = acegiConfig.acegi.loginUserDomainClass
  authorityDomainClassName = acegiConfig.acegi.authorityDomainClass
  requestmapDomainClassName = acegiConfig.acegi.requestMapClass
  Ant.sequential {
    def mailjar = new File("${basedir}/lib/mail-1.4.jar")
    if(!mailjar.exists()){
      println "Downloading mail.jar ..."
      get(dest: "${basedir}/lib/mail-1.4.jar",
              src: "http://repo1.maven.org/maven2/javax/mail/mail/1.4/mail-1.4.jar",
              verbose: true,
              usetimestamp: true)
    }
    def activationjar = new File("${basedir}/lib/activation-1.1.jar")
    if(!activationjar.exists()){
      println "Downloading activation.jar ..."
      get(dest: "${basedir}/lib/activation-1.1.jar",
              src: "http://repo1.maven.org/maven2/javax/activation/activation/1.1/activation-1.1.jar",
              verbose: true,
              usetimestamp: true)
    }
  }
  generateRegistration("register")
}

generateRegistration = {name ->
  def uname = name[0].toUpperCase() + name.substring(1)

  def outFile = new File("${basedir}/grails-app/controllers/${uname}Controller.groovy")

  if (outFile.exists()) {
    Ant.input(addProperty: "overwrite", message: "Do you want to overwrite? y/n")
    def ovw = Ant.antProject.properties."overwrite"
    if (ovw == "y") {
      overwrite = true
    }
  } else {
    overwrite = true
  }

  println "generating files for ${uname} ......."
  def bind = [
          personDomain: "$personDomainClassName",
          authorityDomain: "$authorityDomainClassName",
          requestmapDomain: "$requestmapDomainClassName"]

  //copy the CaptchaController
  println "copying CaptchaController.groovy to - ${basedir}/grails-app/controllers/CaptchaController.groovy "
  Ant.copy(
          file: "${pluginTemplatePath}/controllers/_CaptchaController.groovy",
          tofile: "${basedir}/grails-app/controllers/CaptchaController.groovy", overwrite: overwrite)

  //copy the EmailerService
  println "copying EmailerService.groovy to - ${basedir}/grails-app/services/EmailerService.groovy "
  Ant.copy(
          file: "${pluginTemplatePath}/services/_EmailerService.groovy",
          tofile: "${basedir}/grails-app/services/EmailerService.groovy", overwrite: overwrite)
  //generate RegisterController.groovy
  println "generating file ${basedir}/grails-app/controllers/${uname}Controller.groovy"
  generateFile(
          "${pluginTemplatePath}/controllers/_${uname}Controller.groovy",
          bind,
          "${basedir}/grails-app/controllers/${uname}Controller.groovy")

  //generate views for RegisterController
  println "copying view files to - ${basedir}/grails-app/views/${name}/* "
  Ant.mkdir(dir: "${basedir}/grails-app/views/${name}")
  Ant.copy(
          file:"${pluginTemplatePath}/views/${name}/edit.gsp",
          tofile:"${basedir}/grails-app/views/${name}/edit.gsp", overwrite: overwrite)
  Ant.copy(
          file:"${pluginTemplatePath}/views/${name}/index.gsp",
          tofile:"${basedir}/grails-app/views/${name}/index.gsp", overwrite: overwrite)
  Ant.copy(
          file:"${pluginTemplatePath}/views/${name}/show.gsp",
          tofile:"${basedir}/grails-app/views/${name}/show.gsp", overwrite: overwrite)
}

generateFile = {templateFile, binding, outputPath ->
  def engine = new groovy.text.SimpleTemplateEngine()
  def templateF = new File(templateFile)
  def templateText = templateF.getText()
  def outFile = new File(outputPath)
  if (templateF.exists()) {
    if (overwrite) {
      def template = engine.createTemplate(templateText)
      //println template.make(binding).toString()
      outFile.withWriter {w ->
        template.make(binding).writeTo(w)
      }
      println "file generated at ${outFile.absolutePath}"
    } else {
      println "file *not* generated.: ${outFile.absolutePath} "
    }

  } else {
    println "${templateF} not exists"
  }
}
