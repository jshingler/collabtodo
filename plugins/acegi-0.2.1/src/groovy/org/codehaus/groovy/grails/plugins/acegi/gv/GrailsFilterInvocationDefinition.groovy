/*
 * Copyright 2008 the original author or authors.
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
package org.codehaus.groovy.grails.plugins.acegi.gv;

import org.acegisecurity.ConfigAttributeDefinition
import org.acegisecurity.SecurityConfig
import org.acegisecurity.intercept.web.FilterInvocation
import org.acegisecurity.intercept.web.FilterInvocationDefinitionSource
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.util.AntPathMatcher
import org.springframework.util.StringUtils

/**
 * GrailsFilterInvocationDefinition
 * 
 * @author Tsuyoshi Yamamoto
 * @author Burt Beckwith
 * @since 2008/02/08 18:02:09
 */
public class GrailsFilterInvocationDefinition extends SessionSupport implements FilterInvocationDefinitionSource {

  private static final Log logger = LogFactory.getLog(GrailsFilterInvocationDefinition.class)
  def requestMapClass//="Requestmap"
  def requestMapPathFieldName//="url"
  def requestMapConfigAttributeField//="configAttribute"
  
  def pathMatcher = new AntPathMatcher()
  
  
  //def cTime(stime,name){
  // long etime = System.nanoTime() - stime
  // println "[${name}] " +(etime/1000000) +" ms "
  //}
  
  public ConfigAttributeDefinition lookupAttributes(String url){
    //def s =System.nanoTime()
    url = url.toLowerCase()
    int pos = url.indexOf("?");
    if(pos>0){
      url = url.substring(0, pos);
    }
    
    url=url.replaceAll("\"|'","")
    url=url.replaceAll("//","/")
    
    if (!url.contains(".") || url.indexOf(".gsp")>-1 || url.indexOf(".jsp")>-1) {
      def container = setUpSession()
      try {
         def stn = new StringTokenizer(url,"/")
         def hql="from $requestMapClass where $requestMapPathFieldName = '/' or $requestMapPathFieldName = '/**' "
         def path="/"
         while (stn.hasMoreTokens()) {
           path+=stn.nextToken()
           hql+="or lower($requestMapPathFieldName) like '$path%' "
           path+="/"
         }
         hql+="order by length($requestMapPathFieldName) desc"

         def query = container.session.createQuery(hql)
         def reqestMapList = query/*.setCacheable(true)*/.list()

         if(reqestMapList.size()>0){
           for(reqmap in reqestMapList) {
             boolean matched = pathMatcher.match(reqmap."$requestMapPathFieldName", url)
             if (matched) {
               ConfigAttributeDefinition cad = new ConfigAttributeDefinition()
               def configAttrs = StringUtils.commaDelimitedListToStringArray(reqmap."$requestMapConfigAttributeField")
               for (int i = 0 ;i < configAttrs.length; i++) {
                 String configAttribute = configAttrs[i]
                 cad.addConfigAttribute(new SecurityConfig(configAttribute))
               }
               //cTime(s,"lookupAttributes match ")
               return cad
             }
           }
         }
      }
      finally {
        releaseSession(container)
      }
      //cTime(s,"lookupAttributes ")
    }
    return null
  }

  public Iterator getConfigAttributeDefinitions(){}

  public ConfigAttributeDefinition getAttributes(Object object) throws IllegalArgumentException {
    if ((object == null) || !this.supports(object.getClass())) {
      logger.error("Object must be a FilterInvocation")
      throw new IllegalArgumentException("Object must be a FilterInvocation")
    }
    String url = ((FilterInvocation) object).getRequestUrl()
    return this.lookupAttributes(url)
  }

  public boolean supports(Class clazz) {
    if (FilterInvocation.class.isAssignableFrom(clazz)) {
      return true
    } else {
      return false
    }
  }
  
}