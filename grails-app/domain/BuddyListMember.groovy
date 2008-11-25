
/**
 *  This relates for a member of a budddy list.
 */
class BuddyListMember {
	
    static belongsTo = BuddyList;
	
    String nickName
    User user
    BuddyList buddyList
    
    static constraints = {
		nickName(blank:false)
		user(nullable:false)
    }
    
    String  toString () {
	    return "$nickName - $buddyList?.name - $user?.userName"
    }
}	
