
/**
 * This controller is used to handle buddy list members.
 *
 */
class BuddyListMemberController {


    /**
     * This method will add a person to a user's buddy list.
     * The user will have had to specify a category ahead of time.
     */
    def add = {
        // lets create a member
        def member = new BuddyListMember()
        def user = User.get(params.userNameId)
        member.user = user
        member.buddyList = BuddyList.get(params.buddyListId)
        member.nickName = "${user.lastName}, ${user.firstName}"

        log.info "Member to add - $member"

        member.save()

        // render the template
        render(template:'/common/buddyListMember',  var: 'buddy', collection:BuddyListMember.findAllByBuddyList(member.buddyList))
    }

    /**
     * Delete a buddy list member
     */
    def delete = {

        log.info "Delete Buddy List Member - $params.id"

        // find and remove the member
        def member = BuddyListMember.get(params.id)
        // retrieve the buddy list so we can retrieve it later                
        def buddyList = member.buddyList
        member.delete()
        
        // render the template
        render(template:'/common/buddyListMember',  var: 'buddy', collection:BuddyListMember.findAllByBuddyList(buddyList))
    }

    /**
     * Allows for saving a change to the nickname for a buddy list member.
     */
    def editNickName = {

        log.info "Update buddy list nick name"

        // retrieve member
        def buddyListMember = BuddyListMember.get(params.id)
        buddyListMember.nickName = params.nickName

        // render a new page.
        //render(template:'/common/buddyListMember',  var: 'buddy', collection:BuddyListMember.findAllByBuddyList(buddyListMember.buddyList))
        render params.nickName                
    }
}

