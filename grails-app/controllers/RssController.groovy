import feedsplugin.FeedBuilder

class RssController {
    
    def feed = {
        render(feedType:"atom") { // optional - , feedVersion:"2.0") {
            //title = "Todo List"
            link = "http://localhost:8080/collab-todo/rss"

            Todo.list(sort: "name", order: "asc").each() {
                def todo = it
                entry(it.name) {
                    title = "${todo.name}"
                    link = "http://localhost:8080/collab-todo/todo/view/${todo.id}"
                    author = "${todo.owner.lastName}, ${todo.owner.firstName}"
                    publishedDate = todo.createdDate
                    //link = "http://your.test.server/article/${it.id}"
                    //it.content // return the content
                }
            }
        }
    }
}