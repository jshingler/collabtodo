
/**
 * This class is used to assign keywords to a ToDo.
 * This has many to many relations.  
 */
class Keyword {
	
	String name
	String description
	// possible user relationship? ... very much so?
	
	static belongsTo = Todo
	static hasMany = [todos:Todo]
	
    static constraints = {
		name(blank:false,
          validator: {
			// lets make sure no name matches this description
			if (Keyword.findAllByDescription(it).size() > 0) {
				return false
			}
			return true
		})
	}	
}