<g:if test="${session.user}">

  <div id="buddies">

    <div class="title">Buddies<g:showHide update="addBuddyList"><img border=0 src="${createLinkTo(dir:'images',file:'add_obj.gif')}" alt="[-]"/></g:showHide>
    </div>

      <div id="addBuddyList" style="display:none">
          <g:formRemote name="buddyListForm"
                     url="[controller:'buddyList',action:'add']"
                     update="buddyList"
                     onComplete="showhide('addBuddyList')">
            <input type="text" name="buddyListName" onclick="this.submit();"/>
          </g:formRemote>
      </div>

    <!-- Show Each of the Buddy Lists and their Members -->
    <div id="buddyList">
        <g:render template="/common/buddyList" var="list" collection="${BuddyList.findAllByOwner(session.user)}" />        
    </div>
  </div>

    <script type="text/javascript">
        // This is the auto completer for the first drop down.                
        new Ajax.Autocompleter("autocomplete", "autocomplete_choices", "/collab-todo/user/findUsers", {});

    </script>
  
</g:if>