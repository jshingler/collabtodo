
/**
 * The user class will be the class that will store everything to login
 * as well as contain the infoirmation to retrieve buddy lists, users, etc 
 * off of.
 */
class User {

    // setting a transient object
    static transients = [ "confirmPassword" ]
    
    static hasMany = [ buddyLists : BuddyList, todos: Todo, categories : Category, authorities:Authority ]
    
    static belongsTo = Authority

    static embedded = ['address']
    
    static constraints = {
        userName(blank:false,unique:true)
        password(blank:false, minLength:3)
        email(email:true)
        firstName(blank:false)
        lastName(blank:false)
        address(nullable:true)
    }
    
    String firstName
    String lastName
    String userName
    String password
    String email
    String confirmPassword
    // initialize so we can access it right away
    Address address = new Address()
    
    // needed for acegi
    // needed for acegi to work.
    boolean active = true

    
    String  toString () {
    	return "$lastName, $firstName"
    }
    
    String dump() {
    	return "FirstName: ${firstName}, LastName: ${lastName}, UserName: ${userName}, Password: ${password}, Email: ${email}"
    }
    
}	

/**
 * The address is an embedded class.
 * We choose to do it this way since the address will ONLY be tied to the user.
 * This also shows how you are not limited to one class per file rule with Groovy.
 */


class Address {

    String addressLine1
    String addressLine2
    String city
    String state
    String zipCode
    String country

    static constraints = {
        addressLine1(nullable:true)
        addressLine2(nullable:true)
        city(nullable:true)
        state(nullable:true)
        zipCode(nullable:true)
        country(nullable:true)
    }
}
