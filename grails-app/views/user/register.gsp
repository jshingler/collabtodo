
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Registration</title>
    </head>
    <body>
        <div class="body">
            <h1>Create User</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${user}">
            <div class="errors">
                <g:renderErrors bean="${user}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="handleRegistration" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class='prop'>
                            <td valign='top' class='nameClear'>
                               <label for="login">Login:</label>
                            </td>
                            <td valign='top' class='valueClear ${hasErrors(bean:user,field:'userName','errors')}'>
                               <input type="text" name="userName" value="${user?.userName}"/>
                            </td>
                         </tr>
                         <tr class='prop'>
                            <td valign='top' class='nameClear'>
                               <label for="password">Password:</label>
                            </td>
                            <td valign='top' class='valueClear ${hasErrors(bean:user,field:'password','errors')}'>
                               <input type="password" name="password" value="" />
                            </td>
                         </tr>
                         <tr class='prop'>
                            <td valign='top' class='nameClear'>
                               <label for="confirm">Confirm Password:</label>
                            </td>
                            <td valign='top' class='valueClear ${hasErrors(bean:user,field:'password','errors')}'>
                               <input type="password" name="confirm" value=""/>
                            </td>
                         </tr>
                         <tr class='prop'>
                            <td valign='top' class='nameClear'>
                               <label for="firstName">First Name:</label>
                            </td>
                            <td valign='top' class='valueClear ${hasErrors(bean:user,field:'firstName','errors')}'>
                               <input type="text" name="firstName" value="${user?.firstName}"/>
                            </td>
                         </tr>
                         <tr class='prop'>
                            <td valign='top' class='nameClear'>
                               <label for="lastName">Last Name:</label>
                            </td>
                            <td valign='top' class='valueClear ${hasErrors(bean:user,field:'lastName','errors')}'>
                               <input type="text" name="lastName" value="${user?.lastName}"/>
                            </td>
                         </tr>
                         <tr class='prop'>
                            <td valign='top' class='nameClear'>
                               <label for="email">Email:</label>
                            </td>
                            <td valign='top' class='valueClear ${hasErrors(bean:user,field:'email','errors')}'>
                               <input type="text" name="email" value="${user?.email}"/>
                            </td>
                         </tr>
                         <!-- Address -->
                         <tr class='prop'>
                            <td valign='top' class='nameClear'>
                               <label for="email">Address:</label>
                            </td>
                            <td valign='top' class='valueClear ${hasErrors(bean:user,field:'address','errors')}'>
                               <input type="text" name="address.addressLine1" value="${user?.address?.addressLine1}"/><br/>
                               <input type="text" name="address.addressLine2" value="${user?.address?.addressLine2}"/><br/>
                               <input type="text" name="address.city" value="${user?.address?.city}"/> <input type="text" name="address.state" size="2" value="${user?.address?.state}"/> <input type="text" size="5" name="address.zipCode" value="${user?.address?.zipCode}"/>
                               <input type="text" name="address.country" value="${user?.address?.country}"/>
                            </td>
                         </tr>

                         <!-- non spam code -->
                         <tr class='prop'>
                            <td valign='top' class='nameClear'>
                               <label for="code">Enter Code:</label>
                            </td>
                            <td valign='top' class='valueClear'>
                               <input type="text" name="captcha" value=""><br/>
                               <img src="${createLink(controller:'captcha', action:'index')}" />
                            </td>
                         </tr>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><input class="save" type="submit" value="Register"></input></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
