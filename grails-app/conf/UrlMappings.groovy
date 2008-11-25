class UrlMappings {
    static mappings = {
        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }

        "/$rest/$domain/$id?"{
            controller = "rest"
            action = [GET:"show", PUT:"create", POST:"update", DELETE:"delete"]
            constraints {
                rest(inList:["rest","json"])
            }
        }
    }
}