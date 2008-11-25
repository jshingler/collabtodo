  <div id="menu">
    <nobr>
      <g:isLoggedIn>
          <b>
            <g:loggedInUserInfo
                  field="firstName"/>
            <g:loggedInUserInfo
                  field="lastName"/> |
            <g:link controller="logout"><g:message code="topbar.logout" /></g:link>            
          </b>
      </g:isLoggedIn>
         
      <g:isNotLoggedIn>
        <g:link controller="login">
           <g:message code="topbar.login" /></g:link> 
      </g:isNotLoggedIn>
    </nobr>
  </div>
