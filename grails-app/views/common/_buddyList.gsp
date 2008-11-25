

<div id="buddyListMember{list.id}">
    <g:editInPlace id="buddyListName${list.id}"
                             url="[controller: 'buddyList', action: 'editName', id:list.id]"
                             rows="1"
                             cols= "10"
                             paramName="name">${list.name}</g:editInPlace>

    <g:link controller="buddyList" action="delete" id="${list.id}">
     <img border=0 src="${createLinkTo(dir:'images',file:'delete_obj.gif')}" alt="[-]"/></g:link>
    <g:showHide update="buddyListAdd$list.id">
     <img border=0 src="${createLinkTo(dir:'images',file:'add_obj.gif')}" alt="[*]"/></g:showHide><br />

    <!-- The way to add someone to the form -->
    <div id="buddyListAdd${list.id}" style="display:none">
        <g:formRemote name="buddyListForm${list.id}"
                         url="[controller:'buddyListMember',action:'add']"
                         update="buddyListMembers${list.id}"
                         onComplete="showhide('buddyListAdd${list.id}')">
                <input type="hidden" name="buddyListId" value="${list.id}"/>
                <input type="hidden" name="userNameId" value=""/>
                <input type="text" id="autocomplete${list.id}" name="userId" onclick="this.submit()"/>
                <span id="indicator1" style="display: none"><img src="/collab-todo/images/spinner.gif" alt="Working..." /></span>
                <div id="autocomplete_choices${list.id}" class="autocomplete"></div>

                <script type="text/javascript">
                    new Ajax.Autocompleter("autocomplete${list.id}", "autocomplete_choices${list.id}", "/collab-todo/user/findUsers", {afterUpdateElement : getSelectionId${list.id}});

                    function getSelectionId${list.id}(text, li) {
                        document.buddyListForm${list.id}.userNameId.value = li.id;
                    }
                </script>
        </g:formRemote>
    </div>
    <div id="buddyListMembers${list.id}">
        <g:render template="/common/buddyListMember" var="buddy" collection="${BuddyListMember.findAllByBuddyList(list)}" />        
    </div>
    <br style="line-height: 40%"/>
</div>
