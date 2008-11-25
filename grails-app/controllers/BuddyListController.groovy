/**
 * This is for the buddy list controller.
 */
class BuddyListController {

    /**
     * Adding a buddy.
     */
    def add = {
        log.info "Add a buddy list of $params.buddyListName"
        
        // create our list and save it
        def buddyList = new BuddyList()
        buddyList.name = params.buddyListName
        buddyList.owner = session.user
        buddyList.description = ""
        log.info "BuddyList - $buddyList"
        // TODO - error message if not saved
        buddyList.save()
        
        // render
        render(template:'/common/buddyList',  var: 'list', collection:BuddyList.findAllByOwner(session.user))        
    }

    /**
     * Lists the buddy list.
     */
    def list = {
      log.info "List Buddy List "
      if(!params.max)params.max = 10
      def user = User.get(session.user.id)
      log.info params
      [ buddyListList: BuddyList.findAllByOwner(user, params) ]
    }

    /**
     * Saves the buddy list when adding a new one.
     */
    def save = {
      log.info("Saving BuddyList")
      def bl = new BuddyList(params)
      // grab the user and set him accordingly
      bl.owner = User.get(session.user.id)

      if(bl.save()) {
        redirect(action:list,id:bl.id)
      }
      else {
        render(view:'edit',model:[buddyList:bl])
      }
    }

    /**
     * Called when editing the name.
     * This will save the name change.
     */
    def editName = {

        log.info "Update buddy list name"

        // retrieve member
        def buddyList = BuddyList.get(params.id)
        buddyList.name = params.name

        // render a new page.
        //render(template:'/common/buddyListMember',  var: 'buddy', collection:BuddyListMember.findAllByBuddyList(buddyListMember.buddyList))
        render params.name
    }
}

