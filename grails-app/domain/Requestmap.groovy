/**
 * Domain class for Request Map
 * @author
 * note - this is an auto generated class.
 */
class Requestmap {

	String url
	String configAttribute

	static def constraints = {
		url(blank:false,unique:true)
		configAttribute(blank:false)
	}
}
