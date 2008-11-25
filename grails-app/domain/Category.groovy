

/**
 * The categories are unique per user and used
 * to categorize to-do's by.
 */
class Category {
	
	String name
	String description
	User user
	
	static belongsTo = User
	// TODO - can also do this -   static belongsTo = [ bookmark : Bookmark ]
	static hasMany = [todos: Todo] 
	
	static constraints = {
		name(blank:false)
	}
	
	String toString() {
		return "$name - $description"
	}
	
}	
