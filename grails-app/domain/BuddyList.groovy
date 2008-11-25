

/**
 *
 * This is a buddy list model object.
 * This object will belong to a user and contain members. The members of the buddy list
 * are defiend on the BuddyListMember object.
 */
class BuddyList { 
	static belongsTo = User
	
	static hasMany = [ members : BuddyListMember ]
	
	static constraints = {
		name(blank:false)
		description(blank:true)
		//owner(:false)
	}
	
	 
	String name
	String description
	User owner
	
	String toString() {
		return "$name - $description - $owner.userName"
	}
	
	String dump() {
    	return "Name: ${name}, Description: ${description}, Owner: ${owner}"
    }
}	
