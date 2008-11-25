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

import org.hibernate.Session
import org.hibernate.SessionFactory
import org.springframework.orm.hibernate3.SessionFactoryUtils
import org.springframework.orm.hibernate3.SessionHolder
import org.springframework.transaction.support.TransactionSynchronizationManager

/**
 * SessionSupport
 * @author Tsuyoshi Yamamoto
 * @author Burt Beckwith
 */
public abstract class SessionSupport {
  
  def sessionFactory

  /**
   * set up hibernate session
   */
  protected Map setUpSession() {
    def session
    boolean containerManagedSession
    if (TransactionSynchronizationManager.hasResource(sessionFactory)) {
      if (logger.isDebugEnabled()) logger.debug("Session already has transaction attached")
      containerManagedSession = true
      session = ((SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory)).session
    } else {
      if (logger.isDebugEnabled()) logger.debug("Session does not have transaction attached... Creating new one")
      containerManagedSession = false;
      session = SessionFactoryUtils.getSession(sessionFactory, true);
      SessionHolder sessionHolder = new SessionHolder(session);
      TransactionSynchronizationManager.bindResource(sessionFactory, sessionHolder);
    }
    [session: session, containerManagedSession: containerManagedSession]
  }

  /**
   * Release Session
   */
  protected void releaseSession(Map container) {
    if (!container.containerManagedSession) {
      SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
      SessionFactoryUtils.releaseSession(sessionHolder.getSession(), sessionFactory);
      logger.debug("Session released");
    }
  }
}