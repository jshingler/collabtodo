
/**
 * This is the To Do's Domain object.
 * This object will contain all the To Do's for the application.
 * Chapter 5
 *
 */
class Todo {

    // TODO - upgrade our fixed values to be using Enums when we can
    
    // this makes the class searchable via the plug-in
    static searchable = true
    // Some options
    //static withTable = "todo_tbl"
    
    // TODO - there should be some java 5 support later
//    public enum Priority {
//        HIGH,
//        MEDIUM,
//        LOW };
        
    // TODO is there an implied order that these occur before the domain objects?
    static belongsTo = [User, Category]
    static hasMany = [keywords:Keyword]

	User owner
	Category category
    String name
    String note
    Date createdDate
	Date startDate  // we may get rid of this
    Date dueDate
    Date completedDate
    Date lastModifiedDate
    String priority
    String status
    byte[] associatedFile
    String fileName
    String contentType
    
    static constraints = {
        category(nullable:true)
        dueDate(nullable:true)
        associatedFile(nullable:true)
        owner(nullable:false)
    	name(blank:false)
		createdDate(blank:false)
		lastModifiedDate(nullable:true)
		priority(inList:['Low', 'Medium', 'High'])
		status(inList:['Complete', 'Incomplete'])
		note(maxSize:1000)
		// necessary or it will default hte nullable to false on MySQL
		completedDate(nullable:true,
			validator: {
                            // val is this value, obj is the Todo object
                            val, obj ->
                                    if (val != null) {
                                            return val.after(obj.createdDate)
                                    }
                                    return true
			})
		startDate(nullable:true /*,
                    validator: {
                        // if this was already created lets not worry about it
                        if (it?.compareTo(new Date() - 1) < 0 ) {
                            return false
                        }
                        return true
                    }*/)
// We don't want to limit the file in here in theory.
// since we can do it in other places
//		associatedFile(nullable:true,
//			validator: {
//				if (it?.length > 10000) {
//					return false
//				}
//				return true
//			})
	    fileName(nullable:true)
        contentType(nullable:true)
    }
    
    String toString() {
    	return "Name: ${name}, note: ${note}"
    }
	             
	String dump() {
		return "Owner: ${owner}, Name: ${name}, Note: ${note}, Create Date: ${createdDate}, Due Date: ${dueDate}, Complete Date: ${completedDate}, Priority: ${priority}, Status: ${status}"
	}

    // Auto run some itmes.
    def beforeInsert = {
       createdDate = new Date()
        lastModifiedDate = new Date()
       }
    def beforeUpdate = {
       lastModifiedDate = new Date()
    }

}	