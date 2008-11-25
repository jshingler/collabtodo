  
<html>
    <head>
         <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
         <meta name="layout" content="main" />
         <title>Page List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link action="create">New Page</g:link></span>
        </div>
        <div class="body">
           <h1>Page List</h1>
            <g:if test="${flash.message}">
                 <div class="message">
                       ${flash.message}
                 </div>
            </g:if>
           <table>
             <thead>
               <tr>
               
                   	    <g:sortableColumn property="id" title="Id" />
                  
                   	    <g:sortableColumn property="title" title="Title" />
                  
                   	    <g:sortableColumn property="content" title="Content" />
                  
                        <th></th>
               </tr>
             </thead>
             <tbody>
               <g:each in="${pageList}">
                    <tr>
                       
                            <td>${it.id?.encodeAsHTML()}</td>
                       
                            <td>${it.title?.encodeAsHTML()}</td>
                       
                            <td>${it.content?.encodeAsHTML()}</td>
                       
                       <td class="actionButtons">
                            <span class="actionButton"><g:link action="show" id="${it.id}">Show</g:link></span>
                       </td>
                    </tr>
               </g:each>
             </tbody>
           </table>
               <div class="paginateButtons">
                   <g:paginate total="${Page.count()}" />
               </div>
        </div>
    </body>
</html>
