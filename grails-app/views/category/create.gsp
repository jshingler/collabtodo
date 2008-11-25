  
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create Category</title>         
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Category List</g:link></span>
        </div>
        <div class="body">
            <h1>Create Category</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${category}">
            <div class="errors">
                <g:renderErrors bean="${category}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class='prop'><td valign='top' class='name'><label for='name'>Name:</label></td><td valign='top' class='value ${hasErrors(bean:category,field:'name','errors')}'><input type="text" id='name' name='name' value="${category?.name?.encodeAsHTML()}"/></td></tr>
                        
                            <tr class='prop'><td valign='top' class='name'><label for='description'>Description:</label></td><td valign='top' class='value ${hasErrors(bean:category,field:'description','errors')}'><input type="text" id='description' name='description' value="${category?.description?.encodeAsHTML()}"/></td></tr>
                        <!-- 
                            <tr class='prop'><td valign='top' class='name'><label for='user'>User:</label></td><td valign='top' class='value ${hasErrors(bean:category,field:'user','errors')}'><g:select optionKey="id" from="${User.list()}" name='user.id' value="${category?.user?.id}" ></g:select></td></tr>
                         -->
                        
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
