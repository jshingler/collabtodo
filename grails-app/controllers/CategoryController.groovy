
/**
 * Category controller.
 * This is used to change the category.
 */
class CategoryController {
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    def allowedMethods = [delete:'POST', save:'POST', update:'POST']
    
    def beforeInterceptor = {
    	log.trace("${session?.user?.userName} Start action ${controllerName}Controller.${actionName}() : parameters $params")
    }
    
    def afterInterceptor = { model ->
    	log.trace("${session?.user?.userName} End action ${controllerName}Controller.${actionName}() : returns $model")
    }

    def list = {
        if(!params.max)params.max = 10
//        [ categoryList: Category.list( params ) ]
        def user = User.get(session.user.id)
		[ categoryList: Category.findAllByUser(user, params) ]
    }

    def show = {
//        [ category : Category.get( params.id ) ]
        def category = Category.get( params.id )
        
        if (session.user.id != category.user.id) {
    		flash.message = "You can only display your own categories"
    		redirect(action:list)
    		return
        }
	    return [ category : category ]
    }

    def delete = {
        def category = Category.get( params.id )
        
        if (session.user.id != category.user.id) {
    		flash.message = "You can only delete your own categories"
    		redirect(action:list)
    		return
        }

        if(category) {
            category.delete()
            flash.message = "Category ${params.id} deleted."
            redirect(action:list)
        }
        else {
            flash.message = "Category not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def category = Category.get( params.id )
        
        if (session.user.id != category.user.id) {
    		flash.message = "You can only edit your own categories"
    		redirect(action:list)
    		return
        }

        if(!category) {
                flash.message = "Category not found with id ${params.id}"
                redirect(action:list)
        }
        else {
            return [ category : category ]
        }
    }

    def update = {
        def category = Category.get( params.id )
        
        if (session.user.id != category.user.id) {
    		flash.message = "You can only delete your own categories"
    		redirect(action:list)
    		return
        }
        
        def user = User.get(session.user.id);
        if(category) {
             category.properties = params
             category.user = user
            if(category.save()) {
                flash.message = "Category ${params.id} updated."
                redirect(action:show,id:category.id)
            }
            else {
                render(view:'edit',model:[category:category])
            }
        }
        else {
            flash.message = "Category not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def category = new Category()
        category.properties = params
        return ['category':category]
    }

    def save = {
        def category = new Category()
        category.properties = params
        def user = User.get(session.user.id);
        category.user = user
        if(category.save()) {
            flash.message = "Category ${category.id} created."
            redirect(action:show,id:category.id)
        }
        else {
            render(view:'create',model:[category:category])
        }
    }

}