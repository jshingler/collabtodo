class Page { 
	String title
	String content
	static constaints = {
		title(size: 3..127)
		content(size: 0..4095)
	}
}	
