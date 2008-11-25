/*
 * Copyright 2007 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
import feedsplugin.FeedBuilder

/**
 * @author Marc Palmer (marc@anyware.co.uk)
 */
class FeedBuilderTests extends GroovyTestCase {

	void testBuilderRootNodeWithAttribs() {
		def builder = new FeedBuilder()
		
		builder.feed( title: 'Test feed', link:'http://somewhere.com/') {
			entry {
				"Hello world"
			}
		}
		
		def feed = builder.makeFeed("rss","2.0")
		assertEquals "Test feed", feed.title
		assertEquals "http://somewhere.com/", feed.link
	}
	
	void testBuilderRootNodeWithAttribsAndPropertySetting() {
		def builder = new FeedBuilder()
		builder.feed(title:'Test feed') {
			link = 'http://somewhere.com/'
			entry {
				"Hello world"
			}
		}
		
		def feed = builder.makeFeed("rss","2.0")
		assertEquals "Test feed", feed.title
		assertEquals "http://somewhere.com/", feed.link
	}

	void testBuilderEntries() {
		def builder = new FeedBuilder()
	
		def pubDate = new Date()
		
		builder.feed( title: 'Test feed', link:'http://somewhere.com/') {
			entry('entry one') {
				"Hello"
			}
			entry(title:'entry two',link:'http://somewhere.com/entry2') {
				"Hello"
			}
			entry([title:'entry three',link:'http://somewhere.com/entry3']) {
				"Hello"
			} 
			entry {
				"Hello"
			}
			entry(title:'entry five', publishedDate: pubDate) {
				"Hello"
			}
		}

		def feed = builder.makeFeed("rss","2.0")

		assertEquals "entry one", feed.entries[0].title
		assertEquals "Hello", feed.entries[0].contents[0].value
		
		assertEquals "entry two", feed.entries[1].title
		assertEquals 'http://somewhere.com/entry2', feed.entries[1].link
		assertEquals "Hello", feed.entries[1].contents[0].value

		assertEquals "entry three", feed.entries[2].title
		assertEquals 'http://somewhere.com/entry3', feed.entries[2].link
		assertEquals "Hello", feed.entries[2].contents[0].value

		assertEquals "Hello", feed.entries[3].contents[0].value

		assertEquals "Hello", feed.entries[4].contents[0].value
		assertEquals pubDate, feed.entries[4].publishedDate
	}

	void testBuilderContent() {
		def builder = new FeedBuilder()
	
		builder.feed( title: 'Test feed', link:'http://somewhere.com/') {
			description = "This is a test feed" 
			
			entry {
				"One"
			}
			entry {
				content("Two")
			}
			entry {
				content([type:'text/html']) {
					"<p>Three</p>"
				}
			}
			entry {
				content(type:'text/html') {
					"<p>Four</p>"
				}
			}
			entry {
				content {
					type = 'text/html'
					return "<p>Five</p>"
				}
			}
			entry {
				content {
					type = 'text/html'
					return "<p>Six A</p>"
				}
				content {
					type = 'text/html'
					return "<p>Six B</p>"
				}
			}
		}

		def feed = builder.makeFeed("rss","2.0")

		assertEquals "One", feed.entries[0].contents[0].value
		assertEquals "text/plain", feed.entries[0].contents[0].type

		assertEquals "Two", feed.entries[1].contents[0].value
		assertEquals "text/plain", feed.entries[1].contents[0].type

		assertEquals "<p>Three</p>", feed.entries[2].contents[0].value
		assertEquals "text/html", feed.entries[2].contents[0].type

		assertEquals "<p>Four</p>", feed.entries[3].contents[0].value
		assertEquals "text/html", feed.entries[3].contents[0].type
		
		assertEquals "<p>Five</p>", feed.entries[4].contents[0].value
		assertEquals "text/html", feed.entries[4].contents[0].type

		assertEquals "<p>Six A</p>", feed.entries[5].contents[0].value
		assertEquals "text/html", feed.entries[5].contents[0].type
		assertEquals "<p>Six B</p>", feed.entries[5].contents[1].value
		assertEquals "text/html", feed.entries[5].contents[1].type
	}

	void testBuilderValidation() {
		def builder = new FeedBuilder()

		shouldFail {
			builder.feed( title: 'Test feed', link:'http://somewhere.com/') {
				entry {
					entry {
						"Invalid"
					}
				}
			}
		}

		// We can't assert that this is an error, as the last expression in a closure would 
		// always break it
/*
		builder = new FeedBuilder()
		shouldFail {
			builder.feed( title: 'Test feed', link:'http://somewhere.com/') {
				"Invalid"
			}
		}
*/
		builder = new FeedBuilder()
		shouldFail {
			builder.feed( title: 'Test feed', link:'http://somewhere.com/') {
				content("Invalid")
			}
		}

		builder = new FeedBuilder()
		shouldFail {
			builder.feed( title: 'Test feed', link:'http://somewhere.com/') {
				entry {
					content {
						content()
					}
				}
			}
		}

		builder = new FeedBuilder()
		shouldFail {
			builder.feed( title: 'Test feed', link:'http://somewhere.com/') {
				entry {
					content {
						entry()
					}
				}
			}
		}

		builder = new FeedBuilder()
		shouldFail {
			builder.feed( title: 'Test feed', link:'http://somewhere.com/') {
				content {
					entry {
						"Invalid"
					}
				}
			}
		}
	}
	

}

