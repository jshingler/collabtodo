acegi {
	loadAcegi=true

	algorithm="MD5"
	//use Base64 text ( true or false )
	encodeHashAsBase64=false
	errorPage="null"

	/** login user domain class name and fields */
//	loginUserDomainClass="Person"
//	userName="username"
//	password="passwd"
//	enabled="enabled"
//	relationalAuthorities = "authorities"
	loginUserDomainClass="User"
	userName="userName"
	password="password"
	enabled="active"
	relationalAuthorities = "authorities"

	/**
	 * Authority domain class authority field name
	 * authorityFieldInList
	 */
	authorityDomainClass="Authority"
	authorityField="authority"

	/** use RequestMap from DomainClass */
	useRequestMapDomainClass = true
	/** Requestmap domain class (if useRequestMapDomainClass = true) */
	requestMapClass="Requestmap"
	requestMapPathField="url"
	requestMapConfigAttributeField="configAttribute"
}