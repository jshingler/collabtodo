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
package org.codehaus.groovy.grails.plugins.acegi.gv;

import org.acegisecurity.GrantedAuthority
import org.acegisecurity.userdetails.User

/**
 * extends acegi's User class 
 * to can set Grails Domain Class at login,
 * for able to load auth class from context
 * @author T.Yamamoto
 *
 */
class GrailsUser extends User {

  private static final long serialVersionUID = 6089520028447407158L;
  /** Grails Domain class Object */
  def domainClass

  public GrailsUser(String username, String password, boolean enabled, boolean accountNonExpired, 
        boolean credentialsNonExpired, boolean accountNonLocked, GrantedAuthority[] authorities) 
          throws IllegalArgumentException {
      super(username, password, enabled, accountNonExpired, credentialsNonExpired,
      accountNonLocked, authorities)
  }

}