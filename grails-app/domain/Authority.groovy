/**
 * Authority class for Authority
 * note - this is an auto generated class.
 */
class Authority {

	static hasMany=[people:User]

	/** description */
	String description
	/** ROLE String */
	String authority="ROLE_"

	static def constraints = {
		authority(blank:false)
		description()
	}
}