/* Copyright 2006-2007 the original author or authors.
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
import org.springframework.core.annotation.AnnotationUtils
import org.acegisecurity.annotation.Secured
import org.acegisecurity.annotation.SecurityAnnotationAttributes
import org.acegisecurity.intercept.method.MethodDefinitionAttributes
import org.codehaus.groovy.grails.plugins.acegi.gv.*
import org.codehaus.groovy.grails.commons.*

//import org.codehaus.groovy.grails.plugins.acegi.QuietMethodSecurityInterceptor

/**
 * Grails Spring Security(Acegi Security) Plugin
 * 
 * @author T.Yamamoto
 * @auther Haotian Sun
 */
class AcegiGrailsPlugin {
	def version = "0.2.1"
	def author = "Tsuyoshi Yamamoto"
	def authorEmail = "tyama@xmldo.jp"
	def title = "Spring Security (Acegi Security) on Grails Plugin"
	def description = '''Plugin to use grails domain class from the Spring Security(Acegi Security) and secure your applications with Spring Security(Acegi Security) filters.
	'''
	def documentation ="http://docs.codehaus.org/display/GRAILS/AcegiSecurity+Plugin"
  def observe = ['controllers']
  def loadAfter=['controllers']
  def watchedResources = ["file:./grails-app/controllers/**/*Controller.groovy",
          "file:./plugins/*/grails-app/controllers/**/*Controller.groovy"]

  def getAcegiConfig(){
    def conf
    try {
       ClassLoader parent = getClass().getClassLoader()
       GroovyClassLoader loader = new GroovyClassLoader(parent)
       def ac = loader.loadClass("AcegiConfig")
       def dac = loader.loadClass("DefaultAcegiConfig")
       def _user_config = new ConfigSlurper().parse(ac)
       def _default_config = new ConfigSlurper().parse(dac)

       def config
       if(_user_config){
         config = _default_config.merge(_user_config)
       }else{
         config = _default_config
       }
       conf = config.acegi
    }catch(Exception e) {
      println '"AcegiConfig" not found'
    }
    return conf
  }

  def doWithSpring = {
    def conf = getAcegiConfig()

    if(conf && conf.loadAcegi){
      log.info("loading acegi config ...")
      def useMail = conf.useMail

      def makeItGetter = { field ->
        if(!field){return null}
        "get" + field[0].toUpperCase() + field.substring(1)	
      }

      /** filter list */
      def filters = []
        filters << "httpSessionContextIntegrationFilter"
        filters << "logoutFilter"
        filters << "authenticationProcessingFilter"
        if(conf.basicProcessingFilter) filters << "basicProcessingFilter"
        filters << "securityContextHolderAwareRequestFilter"
        filters << "rememberMeProcessingFilter"
        filters << "anonymousProcessingFilter"
        filters << "exceptionTranslationFilter"
        filters << "filterInvocationInterceptor"
        if(conf.switchUserProcessingFilter) filters << "switchUserProcessingFilter"

			/** filterChainProxy */
			filterChainProxy(org.acegisecurity.util.FilterChainProxy){
				filterInvocationDefinitionSource="""
					CONVERT_URL_TO_LOWERCASE_BEFORE_COMPARISON
					PATTERN_TYPE_APACHE_ANT
					/**=${filters.join(',')}
					"""
			}

			/** httpSessionContextIntegrationFilter */
			httpSessionContextIntegrationFilter(org.acegisecurity.context.HttpSessionContextIntegrationFilter){}

			/** logoutFilter */
			logoutFilter(GrailsLogoutFilter,"/",ref("rememberMeServices")){}

      /** authenticationProcessingFilter */
      authenticationProcessingFilter(GrailsAuthenticationProcessingFilter){
        authenticationManager = ref("authenticationManager")
        authenticationFailureUrl = conf.authenticationFailureUrl //"/login/authfail?login_error=1"
        defaultTargetUrl = conf.defaultTargetUrl // "/"
        filterProcessesUrl = conf.filterProcessesUrl //"/j_acegi_security_check"
        rememberMeServices = ref("rememberMeServices")
      }
      
      //Basic Auth
      if(conf.basicProcessingFilter){
        basicProcessingFilter(org.acegisecurity.ui.basicauth.BasicProcessingFilter){
          authenticationManager=ref("authenticationManager")
          authenticationEntryPoint=ref("basicProcessingFilterEntryPoint")
        }
        basicProcessingFilterEntryPoint(org.acegisecurity.ui.basicauth.BasicProcessingFilterEntryPoint){
          realmName="Grails Realm"
        }
      }

			/** securityContextHolderAwareRequestFilter */
			securityContextHolderAwareRequestFilter(org.acegisecurity.wrapper.SecurityContextHolderAwareRequestFilter){}

			/** anonymousProcessingFilter */
			anonymousProcessingFilter(org.acegisecurity.providers.anonymous.AnonymousProcessingFilter){
				key = conf.key // "foo"
				userAttribute = conf.userAttribute //"anonymousUser,ROLE_ANONYMOUS"
			}
      
			/** rememberMeProcessingFilter */
			rememberMeProcessingFilter(org.acegisecurity.ui.rememberme.RememberMeProcessingFilter){
				authenticationManager=ref("authenticationManager")
				rememberMeServices=ref("rememberMeServices")
			}
			/** rememberMeServices */
			rememberMeServices(org.acegisecurity.ui.rememberme.TokenBasedRememberMeServices){
				userDetailsService=ref("userDetailsService")
				key="grailsRocks"
				cookieName=conf.cookieName
				alwaysRemember=conf.alwaysRemember
				tokenValiditySeconds=conf.tokenValiditySeconds
				parameter=conf.parameter
			}

			/** exceptionTranslationFilter */
			exceptionTranslationFilter(org.acegisecurity.ui.ExceptionTranslationFilter){
				authenticationEntryPoint=ref("authenticationEntryPoint")
				accessDeniedHandler=ref("accessDeniedHandler")
			}

			authenticationEntryPoint(org.codehaus.groovy.grails.plugins.acegi.WithAjaxAuthenticationProcessingFilterEntryPoint){
				loginFormUrl= conf.loginFormUrl // "/login/auth"
				forceHttps= conf.forceHttps // "false"
				ajaxLoginFormUrl=conf.ajaxLoginFormUrl // "/login/authAjax"
				if(conf.ajaxHeader) ajaxHeader=conf.ajaxHeader //default: X-Requested-With
			}
			accessDeniedHandler(org.codehaus.groovy.grails.plugins.acegi.GrailsAccessDeniedHandlerImpl){
				errorPage= conf.errorPage=="null"?null:conf.errorPage // "/login/denied" or 403
				ajaxErrorPage= conf.ajaxErrorPage
				if(conf.ajaxHeader) ajaxHeader=conf.ajaxHeader //default: X-Requested-With
			}

			/** filterInvocationInterceptor */
			filterInvocationInterceptor(org.acegisecurity.intercept.web.FilterSecurityInterceptor){
				authenticationManager=ref("authenticationManager")
				accessDecisionManager=ref("accessDecisionManager")
				if( conf.useRequestMapDomainClass ){
					objectDefinitionSource=ref("objectDefinitionSource")
				}else{
					objectDefinitionSource=conf.requestMapString
				}
			}

			/** accessDecisionManager */
			accessDecisionManager(org.acegisecurity.vote.AffirmativeBased){
				allowIfAllAbstainDecisions="false"
				decisionVoters=[
					ref("roleVoter"),
					ref("authenticatedVoter")]
			}
			roleVoter(org.acegisecurity.vote.RoleVoter){}
			authenticatedVoter(org.acegisecurity.vote.AuthenticatedVoter){}

      if( conf.useRequestMapDomainClass ){
        objectDefinitionSource(GrailsFilterInvocationDefinition){
         requestMapClass=conf.requestMapClass
         requestMapPathFieldName=conf.requestMapPathField
         requestMapConfigAttributeField=conf.requestMapConfigAttributeField
         sessionFactory=ref("sessionFactory")
        }
      }

			/** ProviderManager */
			authenticationManager(org.acegisecurity.providers.ProviderManager){
				providers=[
					ref("daoAuthenticationProvider"),
					ref("anonymousAuthenticationProvider"),
					ref("rememberMeAuthenticationProvider")]
			}
			
			
			/** daoAuthenticationProvider */
			daoAuthenticationProvider(org.acegisecurity.providers.dao.DaoAuthenticationProvider){
				userDetailsService=ref("userDetailsService")
				passwordEncoder=ref("passwordEncoder")
				userCache=ref("userCache")
			}

			/** userCache */
			userCache(org.acegisecurity.providers.dao.cache.EhCacheBasedUserCache){
				cache=ref("cache")
			}
			cache(org.springframework.cache.ehcache.EhCacheFactoryBean){
				cacheManager=ref("cacheManager")
				cacheName="userCache"
			}
			cacheManager(org.springframework.cache.ehcache.EhCacheManagerFactoryBean){}

			/** anonymousAuthenticationProvider */
			anonymousAuthenticationProvider(org.acegisecurity.providers.anonymous.AnonymousAuthenticationProvider){
				key= conf.key // "foo"
			}
			/** rememberMeAuthenticationProvider */
			rememberMeAuthenticationProvider(org.acegisecurity.providers.rememberme.RememberMeAuthenticationProvider){
				key="grailsRocks"
			}
			
			/** passwordEncoder */
			passwordEncoder(org.acegisecurity.providers.encoding.MessageDigestPasswordEncoder,conf.algorithm){
				if(conf.encodeHashAsBase64) encodeHashAsBase64=true
			}

      /** DetailsService */
      userDetailsService(GrailsDaoImpl){
        userName= conf.userName
        password= conf.password
        enabled= conf.enabled
        authority= conf.authorityField
        loginUserDomainClass=conf.loginUserDomainClass
        relationalAuthorities=conf.relationalAuthorities
        authoritiesMethod=conf.getAuthoritiesMethod
        sessionFactory=ref("sessionFactory")
      }

			/** LoggerListener ( log4j.logger.org.acegisecurity=info,stdout ) */
			if(conf.useLogger) loggerListener(org.acegisecurity.event.authentication.LoggerListener){}

			/** experiment on Annotation and MethodSecurityInterceptor .
			 *  for secure services
			 */
			serviceSecureAnnotation(SecurityAnnotationAttributes){}
			serviceSecureAnnotationODS(MethodDefinitionAttributes){
				attributes=ref("serviceSecureAnnotation")
			}
			/** securityInteceptor */
			securityInteceptor(org.codehaus.groovy.grails.plugins.acegi.QuietMethodSecurityInterceptor){
				validateConfigAttributes=false
				authenticationManager=ref("authenticationManager")
				accessDecisionManager=ref("accessDecisionManager")
				objectDefinitionSource=ref("serviceSecureAnnotationODS")
				throwException=true
			}

			/** check Annotation */
			def checkAnnotations ={cls->
				def result=false
				cls.methods.each{method->
					def annotations = method.getAnnotations()
					if(annotations.size()>0){
						annotations.each{annotation->
							if(annotation instanceof Secured){
								//println annotation
								result=true
							}
						}
					}
				}
				return result
			}
			//load Services which has Annotations
			application.serviceClasses.each { serviceClass ->
				if(checkAnnotations(serviceClass.clazz)){
					"${serviceClass.propertyName}Sec"(org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator){
						beanNames="${serviceClass.propertyName}"
						interceptorNames=["securityInteceptor"]
						proxyTargetClass=true
					}
				}
			}

      //load simple java mail settings
      if (useMail) {
        mailSender(org.springframework.mail.javamail.JavaMailSenderImpl) {
          host = conf.mailHost
          username = conf.mailUsername
          password = conf.mailPassword
          protocol = conf.mailProtocol
        }
        mailMessage(org.springframework.mail.SimpleMailMessage) {
          from = conf.mailFrom
        }
      }
      
      //Switch User 
      if(conf.switchUserProcessingFilter){
        switchUserProcessingFilter(org.acegisecurity.ui.switchuser.SwitchUserProcessingFilter){
          userDetailsService=ref("userDetailsService")
          switchUserUrl=conf.swswitchUserUrl
          exitUserUrl=conf.swexitUserUrl
          targetUrl=conf.swtargetUrl
        }
      }

    }else{
      log.info("[loadAcegi=false] Acegi Security will not loaded")
    }

	}
  def doWithApplicationContext = { applicationContext ->
    // TODO Implement post initialization spring config (optional)		
  }
  def doWithWebDescriptor = { xml ->

    ClassLoader parent = getClass().getClassLoader()
    GroovyClassLoader loader = new GroovyClassLoader(parent)
    def config
    def _user_config
    try {
      def ac = loader.loadClass("AcegiConfig")
      _user_config = new ConfigSlurper().parse(ac)
    }catch(Exception e) {
      println "AcegiConfig not found"
    }
    
    def dac = loader.loadClass("DefaultAcegiConfig")
    if(_user_config){
      log.info("using user AcegiConfig")
      def _default_config = new ConfigSlurper().parse(dac)
      config = _default_config.merge(_user_config)
    }else{
      log.info("using DefaultAcegiConfig")
      config = new ConfigSlurper().parse(dac)
    }

    def conf = config.acegi
    //println "***** ${conf.loadAcegi}"
    if(conf && conf.loadAcegi){
      def contextParam = xml."context-param"
      contextParam[contextParam.size()-1]+{
        'filter' {
          //'filter-name'('acegiAuthenticationProcessingFilter')
          //'filter-class'('org.acegisecurity.util.FilterToBeanProxy')
          'filter-name'('filterChainProxy')
          'filter-class'('org.springframework.web.filter.DelegatingFilterProxy')
          'init-param'{
            'param-name'('targetClass')
            'param-value'('org.acegisecurity.util.FilterChainProxy')
          }
        }
      }

      def filter = xml."filter"
      filter[filter.size()-1]+{
        'filter-mapping'{
          //'filter-name'('acegiAuthenticationProcessingFilter')
          'filter-name'('filterChainProxy')
          'url-pattern'("/*")
        }
      }
    }
  }

  def doWithDynamicMethods = { ctx ->
    for (con in application.controllerClasses) {
      MetaClass mc = con.metaClass
      registerControllerProps(mc)
    }
  }
  
  def registerControllerProps(MetaClass mc){
    mc.getAuthUserDomain<<{->
      def principal = org.acegisecurity.context.SecurityContextHolder.context?.authentication?.principal
      if( principal!=null && principal!="anonymousUser"){
        return principal?.domainClass
      }else{
        return null
      }
    }
    mc.getPrincipalInfo<<{->
      return org.acegisecurity.context.SecurityContextHolder.context?.authentication?.principal
    }
    mc.isUserLogon<<{->
      def principal = org.acegisecurity.context.SecurityContextHolder.context?.authentication?.principal
      if( principal!=null && principal!="anonymousUser"){
        return true
      }else{
        return false
      }
    }
  }
  
  def onChange = { event ->
    if (application.isArtefactOfType(ControllerArtefactHandler.TYPE, event.source)) {
      def controllerClass = application.addArtefact(ControllerArtefactHandler.TYPE, event.source)
      MetaClass mc = controllerClass.metaClass
      registerControllerProps(mc)
    }
  }
  def onApplicationChange = { event ->
  }
}
