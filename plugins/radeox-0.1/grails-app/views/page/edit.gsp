  
<html>
    <head>
         <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
         <meta name="layout" content="main" />
         <title>Edit Page</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link action="list">Page List</g:link></span>
            <span class="menuButton"><g:link action="create">New Page</g:link></span>
        </div>
        <div class="body">
           <h1>Edit Page</h1>
           <g:if test="${flash.message}">
                 <div class="message">${flash.message}</div>
           </g:if>
           <g:hasErrors bean="${page}">
                <div class="errors">
                    <g:renderErrors bean="${page}" as="list" />
                </div>
           </g:hasErrors>
           <div class="prop">
	      <span class="name">Id:</span>
	      <span class="value">${page?.id}</span>
           </div>           
           <g:form controller="page" method="post" >
               <input type="hidden" name="id" value="${page?.id}" />
               <div class="dialog">
                <table>
                    <tbody>

                       
                       
				<tr class='prop'><td valign='top' class='name'><label for='title'>Title:</label></td><td valign='top' class='value ${hasErrors(bean:page,field:'title','errors')}'><input type="text" name='title' value="${page?.title?.encodeAsHTML()}"/></td></tr>
                       
				<tr class='prop'><td valign='top' class='name'><label for='content'>Content:</label></td><td valign='top' class='value ${hasErrors(bean:page,field:'content','errors')}'>
                    
                    <g:textArea name="content" value="${page?.content?.encodeAsHTML()}" rows="5" cols="78"/>

                </td></tr>
                       
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
