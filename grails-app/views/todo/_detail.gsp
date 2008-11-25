
<div id="todoDetail${todo.id}" class="todo">
  <div class="todoTitle">${todo.name?.encodeAsHTML()}
     <g:link action="edit" id="${todo.id}"><img border=0 src="${createLinkTo(dir:'images',file:'write_obj.gif')}" alt="[EDIT]"/></g:link>
     <g:remoteLink
             action="removeTask"
             id="${todo.id}"
             update="todoDetail$todo.id"
             onComplete="highlight('todoDetail$todo.id');">
         <img border=0 src="${createLinkTo(dir:'images',file:'delete_obj.gif')}" alt="[EDIT]"/>
     </g:remoteLink>

      <g:showHide update="todoDetailFull${todo.id}"><img border=0 src="${createLinkTo(dir:'images',file:'add_obj.gif')}" alt="[Show All]"/></g:showHide>
  </div>

  <div id="todoDetailFull${todo.id}" class="todo" style="display:none">
    Status: ${todo.status?.encodeAsHTML()} <br />
    Priority: ${todo.priority?.encodeAsHTML()} <br />
    Created date: ${todo.createdDate?.encodeAsHTML()} <br />
    Last Modified date: ${todo.lastModifiedDate?.encodeAsHTML()} <br />

    <g:if test="${todo.completedDate == null}">
        Complete Task: <input type="checkbox"
                              onclick="${remoteFunction(
                                            action:'completeTask',
                                            id:todo.id,
                                            update:'todoDetail' + todo.id,
                                            onComplete:'highlight(\'todoDetail' + todo.id+'\')' )};"/> <br />
    </g:if>
    <g:else>
        Completed Date: ${todo.completedDate?.encodeAsHTML()} <br />
    </g:else>
    <g:if test="${todo.fileName?.length() > 0}">
        <g:link action="downloadFile" id="${todo.id}">Download Attachment</g:link><br/>
    </g:if>
    <!-- show notes -- mark in the code that we should use a todo -->
    <g:radeoxRender>${todo?.note}</g:radeoxRender>
    <!-- update:[success:'great', failure:'ohno'], -->
      <!--
    <g:remoteLink action="showNotes" id="${todo.id}" update="todoDetailNote${todo.id}"> Notes</g:remoteLink><br/>
    <div id="todoDetailNote${todo.id}">
    </div>
    -->
    <hr/>
  </div>

</div>