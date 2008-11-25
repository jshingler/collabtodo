  
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create Todo</title>         
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Todo List</g:link></span>
        </div>
        <div class="body">
            <h1>Create Todo</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${todo}">
            <div class="errors">
                <g:renderErrors bean="${todo}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save2" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        <!-- 
                            <tr class='prop'><td valign='top' class='name'><label for='owner'>Owner:</label></td><td valign='top' class='value ${hasErrors(bean:todo,field:'owner','errors')}'><g:select optionKey="id" from="${User.list()}" name='owner.id' value="${todo?.owner?.id}" ></g:select></td></tr>
                         -->
                        
                            <tr class='prop'><td valign='top' class='name'><label for='name'>Name:</label></td><td valign='top' class='value ${hasErrors(bean:todo,field:'name','errors')}'><input type="text" id='name' name='name' value="${todo?.name?.encodeAsHTML()}"/></td></tr>                       
                        
                            <tr class='prop'><td valign='top' class='name'><label for='dueDate'>Start Date:</label></td><td valign='top' class='value ${hasErrors(bean:todo,field:'dueDate','errors')}'><g:datePicker name='dueDate' value="${todo?.dueDate}" noSelection="['':'']"></g:datePicker></td></tr>
                        
                            <tr class='prop'><td valign='top' class='name'><label for='priority'>Priority:</label></td><td valign='top' class='value ${hasErrors(bean:todo,field:'priority','errors')}'><input type="text" id='priority' name='priority' value="${todo?.priority?.encodeAsHTML()}"/></td></tr>
                        
                            <tr class='prop'><td valign='top' class='name'><label for='status'>Status:</label></td><td valign='top' class='value ${hasErrors(bean:todo,field:'status','errors')}'><input type="text" id='status' name='status' value="${todo?.status?.encodeAsHTML()}"/></td></tr>
                        
                            <tr class='prop'><td valign='top' class='name'><label for='completedDate'>Completed Date:</label></td><td valign='top' class='value ${hasErrors(bean:todo,field:'completedDate','errors')}'><g:datePicker name='completedDate' value="${todo?.completedDate}" noSelection="['':'']"></g:datePicker></td></tr>
                        
                            <tr class='prop'><td valign='top' class='name'><label for='note'>Note:</label></td><td valign='top' class='value ${hasErrors(bean:todo,field:'note','errors')}'><textarea rows='5' cols='40' name='note'>${todo?.note?.encodeAsHTML()}</textarea></td></tr>
                        
                            <tr class='prop'><td valign='top' class='name'><label for='category'>Category:</label></td><td valign='top' class='value ${hasErrors(bean:todo,field:'category','errors')}'><g:select optionKey="id" from="${Category.list()}" name='category.id' value="${todo?.category?.id}" ></g:select></td></tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><input class="save" type="submit" value="Create"></input></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
