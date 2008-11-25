            
class PageController {
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only
    // accept POST requests
    def allowedMethods = [delete:'POST',
                          save:'POST',
                          update:'POST']

    def list = {
        if(!params.max)params.max = 10
        [ pageList: Page.list( params ) ]
    }

    def show = {
        [ page : Page.get( params.id ) ]
    }

    def delete = {
        def page = Page.get( params.id )
        if(page) {
            page.delete()
            flash.message = "Page ${params.id} deleted."
            redirect(action:list)
        }
        else {
            flash.message = "Page not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def page = Page.get( params.id )

        if(!page) {
                flash.message = "Page not found with id ${params.id}"
                redirect(action:list)
        }
        else {
            return [ page : page ]
        }
    }

    def update = {
        def page = Page.get( params.id )
        if(page) {
             page.properties = params
            if(page.save()) {
                redirect(action:show,id:page.id)
            }
            else {
                render(view:'edit',model:[page:page])
            }
        }
        else {
            flash.message = "Page not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def page = new Page()
        page.properties = params
        return ['page':page]
    }

    def save = {
        def page = new Page()
        page.properties = params
        if(page.save()) {
            redirect(action:show,id:page.id)
        }
        else {
            render(view:'create',model:[page:page])
        }
    }

}