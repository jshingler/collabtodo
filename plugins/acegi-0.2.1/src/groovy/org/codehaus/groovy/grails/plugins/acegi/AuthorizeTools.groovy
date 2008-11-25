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
package org.codehaus.groovy.grails.plugins.acegi;

import org.acegisecurity.GrantedAuthorityImpl
import org.acegisecurity.context.SecurityContextHolder as SCH
import org.springframework.util.StringUtils as STU
import org.springframework.web.servlet.support.RequestContextUtils as RCU
import org.apache.commons.codec.digest.DigestUtils as DU

/**
 * 
 * @author Tsuyoshi Yamamoto
 * @since 2008/02/29 13:02:29
 */
class AuthorizeTools {
  
  
  def authoritiesToRoles(def c) {
    Set target = new HashSet()
    c.each {authority->
      if (null == authority.getAuthority()) {
        throw new IllegalArgumentException(
          "Cannot process GrantedAuthority objects which return null from getAuthority() - attempting to process "
            + authority)
      }
      target.add(authority.authority)
    }
    return target;
  }

  def getPrincipalAuthorities() {
    def currentUser = SCH.context.authentication
    if (null == currentUser) {
      return Collections.EMPTY_LIST;
    }
    if ((null == currentUser.authorities) || (currentUser.authorities.length < 1)) {
      return Collections.EMPTY_LIST;
    }
    def granted = Arrays.asList(currentUser.authorities);

    return granted;
  }

  def parseAuthoritiesString(def authorizationsString) {
    def requiredAuthorities = new HashSet()
    def authorities = STU.commaDelimitedListToStringArray(authorizationsString)
    authorities.each {auth->
      requiredAuthorities.add(new GrantedAuthorityImpl(auth))
    }
    return requiredAuthorities
  }

  def retainAll(def granted, def required) {
    def grantedRoles = authoritiesToRoles(granted);
    def requiredRoles = authoritiesToRoles(required);
    grantedRoles.retainAll(requiredRoles);

    return rolesToAuthorities(grantedRoles, granted);
  }

  def rolesToAuthorities(def grantedRoles, def granted) {
    def target = new HashSet()
    grantedRoles.each {role->
      def auth = granted.find{authority->authority.authority==role}
      if(auth!=null){
        target.add(auth.authority)
      }
    }
    return target;
  }
  
}