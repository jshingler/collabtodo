
<html>
    <head>
         <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
         <meta name="layout" content="main" />
         <title>Edit Todo</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link action="list">Todo List</g:link></span>
            <span class="menuButton"><g:link action="create">New Todo</g:link></span>
        </div>
        <div class="body">
           <h1>Edit Todo</h1>
           <g:if test="${flash.message}">
                 <div class="message">${flash.message}</div>
           </g:if>
           <g:hasErrors bean="${todo}">
                <div class="errors">
                    <g:renderErrors bean="${todo}" as="list" />
                </div>
           </g:hasErrors>
           <div class="prop">
	      <span class="name">Id:</span>
	      <span class="value">${todo?.id}</span>
           </div>
           <g:form controller="todo" method="post" >
               <input type="hidden" name="id" value="${todo?.id}" />
               <div class="dialog">
                <table>
                    <tbody>



				<tr class='prop'><td valign='top' class='name'><label for='owner'>Owner:</label></td><td valign='top' class='value ${hasErrors(bean:todo,field:'owner','errors')}'><g:select optionKey="id" from="${User.list()}" name='owner.id' value="${todo?.owner?.id}"></g:select></td></tr>

				<tr class='prop'><td valign='top' class='name'><label for='name'>Name:</label></td><td valign='top' class='value ${hasErrors(bean:todo,field:'name','errors')}'><input type="text" name='name' value="${todo?.name?.encodeAsHTML()}"/></td></tr>

				<tr class='prop'><td valign='top' class='name'><label for='createdDate'>Create Date:</label></td><td valign='top' class='value ${hasErrors(bean:todo,field:'createdDate','errors')}'><g:datePicker name='createdDate' value="${todo?.createdDate}"></g:datePicker></td></tr>

				<tr class='prop'><td valign='top' class='name'><label for='priority'>Priority:</label></td><td valign='top' class='value ${hasErrors(bean:todo,field:'priority','errors')}'><input type='text' name='priority' value="${todo?.priority}" /></td></tr>

				<tr class='prop'><td valign='top' class='name'><label for='status'>Status:</label></td><td valign='top' class='value ${hasErrors(bean:todo,field:'status','errors')}'><input type="text" name='status' value="${todo?.status?.encodeAsHTML()}"/></td></tr>

				<tr class='prop'><td valign='top' class='name'><label for='note'>Note:</label></td><td valign='top' class='value ${hasErrors(bean:todo,field:'note','errors')}'><g:textArea type="text" name='note' value="${todo?.note?.encodeAsHTML()}" rows="5" cols="40"/></td></tr>

				<tr class='prop'><td valign='top' class='name'><label for='dueDate'>Due Date:</label></td><td valign='top' class='value ${hasErrors(bean:todo,field:'dueDate','errors')}'><g:datePicker name='dueDate' value="${todo?.dueDate}"></g:datePicker></td></tr>

				<tr class='prop'><td valign='top' class='name'><label for='categories'>Categories:</label></td><td valign='top' class='value ${hasErrors(bean:todo,field:'categories','errors')}'><ul>
    <g:each var='c' in='${todo?.categories?}'>
        <li><g:link controller='category' action='show' id='${c.id}'>${c}</g:link></li>
    </g:each>
</ul>
<g:link controller='category' params='["todo.id":todo?.id]' action='create'>Add Category</g:link>
</td></tr>

				<tr class='prop'><td valign='top' class='name'><label for='completedDate'>Complete Date:</label></td><td valign='top' class='value ${hasErrors(bean:todo,field:'completedDate','errors')}'><g:datePicker name='completedDate' value="${todo?.completedDate}"></g:datePicker></td></tr>

                    </tbody>
                </table>
               </div>

               <div class="buttons">
                     <span class="button"><g:actionSubmit value="Update" /></span>
                     <span class="button"><g:actionSubmit value="Delete" /></span>
               </div>
            </g:form>
        </div>
    </body>
</html>
