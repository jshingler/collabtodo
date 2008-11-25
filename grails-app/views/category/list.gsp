  
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Category List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New Category</g:link></span>
        </div>
        <div class="body">
            <h1>Category List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                        <!-- 
                   	        <g:sortableColumn property="id" title="Id" />
                         -->
                        
                   	        <g:sortableColumn property="name" title="Name" />
                        
                   	        <g:sortableColumn property="description" title="Description" />
                        <!-- 
                   	        <th>User</th>
                         -->
                   	    
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${categoryList}" status="i" var="category">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        <!-- 
                            <td><g:link action="show" id="${category.id}">${category.id?.encodeAsHTML()}</g:link></td>
                            <td>${category.name?.encodeAsHTML()}</td>
                         -->
                        
                            <td><g:link action="show" id="${category.id}">${category.name?.encodeAsHTML()}</g:link></td>
                        
                            <td>${category.description?.encodeAsHTML()}</td>
                        
                        <!-- 
                            <td>${category.user?.encodeAsHTML()}</td>
                         -->
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${Category.count()}" />
            </div>
        </div>
    </body>
</html>
