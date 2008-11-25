<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Todo List</title>
  </head>
  <body>
    <g:preLoadShowHide/>
    <g:javascript library="scriptaculous" />
    <div class="body">
      <h2>Todo List<g:showHide update="addToDo"><img border=0 src="${createLinkTo(dir:'images',file:'add_obj.gif')}" alt="[ADD]"/></g:showHide>
      </h2>
      <div id="messageDisplay">
        <g:if test="${flash.message}">
            <div class="message">
                ${flash.message}
            </div>
        </g:if>
        <g:hasErrors>
          <div class="message">
            <g:eachError bean="${todo}">
              <li><g:message error="${it}" /></li>
            </g:eachError>
          </div>
        </g:hasErrors>
      </div>
      <!-- JavaScript highlight -->
      <script type="text/javascript">
          function highlight(id) {
            //eval("new Effect.Highlight(" + id + ", {startcolor:'#ff99ff', endcolor:'#999999'});");
            eval("new Effect.Highlight(" + id + ", {duration:0.5});");
          }
      </script>

      <!-- Add -->
      <div id="addToDo" style="display:none">
       <g:form name="todoForm"
                     action="${todo?.id > 0 ? 'update' : 'save'}"
                     enctype="multipart/form-data">
               <!--
                    update="[success:'todoList',failure:'error']"
                     update="[success:'todoListId',failure:'error']"
                     onComplete="showhide('addToDo')">
                     - use when using the ajaxForm
                     -->
       <g:hiddenField name="id" value="${todo?.id}" />
           <div class="dialog">
            <table>
                <tbody>
                  <tr class='prop'><td valign='top' class='name'>
                      <label for='category'>Category:</label></td>
                      <td valign='top' class='value ${hasErrors(bean:todo,field:'owner','errors')}'>
                        <g:select optionKey="id" optionValue="name" from="${Category.list()}" name='category.id' value="${todo?.category?.id}">

                        </g:select>
                      </td>
                  </tr>

                  <tr class='prop'><td valign='top' class='name'><label for='name'>Name:</label></td><td valign='top' class='value ${hasErrors(bean:todo,field:'name','errors')}'><input type="text" name='name' value="${todo?.name?.encodeAsHTML()}"/></td></tr>

                  <tr class='prop'><td valign='top' class='name'>
                      <label for='createdDate'>Start Date:</label></td>
                      <td valign='top' class='value ${hasErrors(bean:todo,field:'startDate','errors')}'>
                      <g:datePicker name='startDate' value="${todo?.createdDate}" precision="day" years="${2007..2011}"></g:datePicker></td></tr>


                  <tr class='prop'><td valign='top' class='name'>
                      <label for='priority'>Priority:</label></td>
                      <td valign='top' class='value ${hasErrors(bean:todo,field:'priority','errors')}'>
                      <g:select from="${new Todo().constraints?.priority?.inList}" name='priority' value="${todo?.priority}"/>
                  </td></tr>

                  <tr class='prop'><td valign='top' class='name'><label for='status'>Status:</label></td>
                      <td valign='top' class='value ${hasErrors(bean:todo,field:'status','errors')}'>
                      <g:select from="${new Todo().constraints?.status?.inList}" name='status' value="${todo?.status}"/>
                  </td></tr>

                  <tr class='prop'><td valign='top' class='name'><label for='dueDate'>Due Date:</label></td><td valign='top' class='value ${hasErrors(bean:todo,field:'dueDate','errors')}'><g:datePicker name='dueDate' value="${todo?.dueDate}"></g:datePicker></td></tr>

                  <tr class='prop'><td valign='top' class='name'><label for='dueDate'>File:</label></td><td valign='top' class='value ${hasErrors(bean:todo,field:'associatedFile','errors')}'>
                      <input type="file" name="associatedFile" />
                  </td></tr>

                  <tr class='prop'>
                      <td valign='top' class='name'><label for='note'>Note:</label></td>
                      <td valign='top' class='value ${hasErrors(bean:todo,field:'note','errors')}'>
                      <g:textArea name="note" value="${todo?.note?.encodeAsHTML()}" rows="4" cols="50"/>
                  </td></tr>

                </tbody>
           </table>
           </div>
           <div class="buttons">
               <span class="formButton">
                  <input type="submit" value="${todo?.id > 0 ? 'Update' : 'Create'}"/>
               </span>

               <span class="formButton">
                  <a class="formButton" href="${createLink(controller: 'todo', action: 'list')}">Cancel</a>
               </span>
           </div>
        </g:form>
    </div>

    <!-- This is used to show the addSection in case of an error there -->
    <g:if test="${todo}">
          <script language="JavaScript">
              showhide('addToDo');
          </script>
      </g:if>
        
      <!-- to do list -->
      <div id="todoList">      
        <g:render template="detail" var="todo" collection="${todoList}" />
        <!-- Run the Draggable Items-->
        <script type="text/javascript">
            var todos = document.getElementsByClassName('todoDetail$');
            for (var i = 0; i < todos.length; i++ ) {
                new Draggable(todos[i].id, {ghosting: true, revert: true})

            }
            //Droppables.add('')
            new Draggable('todoDetail1', {ghosting: true, revert: true})            

        </script>
      </div>
	  <collabreports:report id="todoReport" controller="TodoController" 
        action="userTodo" report="userTodo" 
        format="PDF,HTML,CSV,XLS,RTF,TXT,XML">
        <input type="hidden" name="userName" value="${todoList[0]?.owner}" />
      </collabreports:report>
    </div>
  </body>
</html>
