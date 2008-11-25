
<div class="buddy" id="buddy${buddy.id}">

    <g:editInPlace id="buddyEdit${buddy.id}"
                             url="[controller: 'buddyListMember', action: 'editNickName', id:buddy.id]"
                             rows="1"
                             cols= "10"
                             paramName="nickName">${buddy.nickName}</g:editInPlace>   
    <g:remoteLink controller="buddyListMember" action="delete" id="${buddy.id}"
                  update="buddyListMember{buddy.buddyList.id}">
        <img border=0 src="${createLinkTo(dir:'images',file:'delete_obj.gif')}" alt="[-]"/>
    </g:remoteLink>
</div>
<br />


<!-- Let's Add the support for the Droppable -->
<script type="text/javascript">

    Droppables.add('buddy1', []);

</script>