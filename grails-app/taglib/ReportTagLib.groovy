class ReportTagLib {
	
static namespace = 'collabreports'
	
def report = { attrs, body ->
	
	validateAttributes(attrs)
	def appPath = grailsAttributes.getApplicationUri(request)
	
	out << """ 
        <form id=\"${attrs['id']}\" name=\"${attrs['report']}\" action=\"${appPath}/report\">
		<input type=\"hidden\" name=\"format\"/>
		<input type=\"hidden\" name=\"file\" value=\"${attrs['report']}\"/> 
        <input type=\"hidden\" name=\"_controller\" value=\"${attrs['controller']}\"/>
        <input type=\"hidden\" name=\"_action\" value=\"${attrs['action']}\"/>
		"""
	TreeSet formats = attrs['format'].split(",")
	formats.each{
		  	out << """ 
		  		<a href=\"#${attrs['report']}Report\" 
                   onClick=\"document.getElementById('${attrs['id']}').
                    format.value = '${it}';
                    document.getElementById('${attrs['id']}').submit()\">
				<img width=\"16px\" height=\"16px\" border=\"0\" 
                   src=\"${appPath}/images/icons/${it}.gif\" />
				</a>
				"""
	}
	out << body()
	out << "</form>"
}

def report2 = { attrs, body ->

    validateAttributes(attrs)
    def appPath = grailsAttributes.getApplicationUri(request)

    out << """ 
        <form id=\"${attrs['id']}\" name=\"${attrs['report']}\" action=\"${appPath}/report\">
	    <input type=\"hidden\" name=\"format\"/>
	    <input type=\"hidden\" name=\"file\" value=\"${attrs['report']}\"/> 
        <input type=\"hidden\" name=\"_controller\" value=\"${attrs['controller']}\"/>
        <input type=\"hidden\" name=\"_action\" value=\"${attrs['action']}\"/>
	    """

    out << body()
    
    TreeSet formats = attrs['format'].split(",")
    formats.each{
  	   out << """ 
  		    <a href=\"#${attrs['report']}Report\" onClick=\"document.getElementById('${attrs['id']}').format.value = '${it}';document.getElementById('${attrs['id']}').submit()\">
		    <img width=\"16px\" height=\"16px\" border=\"0\" src=\"${appPath}/images/icons/${it}.gif\" />
		    </a>
		    """
    }
    out << "</form>"
}

private void validateAttributes(attrs) {
  //Verify the 'id' attribute
  if(attrs.id == null) 
  	throw new Exception("The 'id' attribute in 'report' tag mustn't be 'null'")

  //Verify the 'format' attribute
  def availableFormats = ["CSV","HTML","RTF","XLS","PDF","TXT","XML"]
  attrs.format.toUpperCase().split(",").each{
    if(!availableFormats.contains(it)){
      throw new Exception("""Value ${it} is a invalid format attribute. 
            Only ${availableFormats} are permitted""")
    }
  }
  
}

}