import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * This controller is the main controller for our site.
 * This is going to add the Todo and its associated files.
 */
class TodoController {

    // the delete, save and update actions only accept POST requests
    def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    /**
     * Redirects to the list.
     */
    def index = { redirect(action:list,params:params) }
	
	def beforeInterceptor = [action:this.&beforeAudit,except:['list']]
    def afterInterceptor = [action:{model ->this.&afterAudit(model)},except:['list']]
    
    def beforeAudit = {
        log.trace("${session?.user?.userName} Start action ${controllerName}Controller.${actionName}() : parameters $params")
    }
  
    def afterAudit = { model ->
        log.trace("${session?.user?.userName} End action ${controllerName}Controller.${actionName}() : returns $model")
    }
	
    /**
     * This is used to download the file.
     * This will lookup the todo and send the file to the output stream.
     */
    def downloadFile = {
        // look up the todo
        def todo = Todo.get( params.id )    

        // send the file to the output stream
        response.setHeader("Content-disposition", "attachment; filename=${todo.fileName}")
        response.contentType = todo.contentType
        response.outputStream << todo.associatedFile
    }

    /**
     * Retrieve all of your todo's
     */
    def list = {
        def user = User.get(session.user.id)
        [ todoList: Todo.findAllByOwner(user) ]
    }

// no longer used as well since the todo's now are all on the one page.
//    def show = {
////        [ todo : Todo.get( params.id ) ]
//        def todo = Todo.get( params.id )
//        if (session.user.id != todo.owner.id) {
//    		flash.message = "You can only display your own Todo"
//    		redirect(action:list)
//    		return
//        }
//        return [ todo : todo ]
//    }

// Used when deleting without ajax.
// keeping it here for reference
//    def delete = {
//        def todo = Todo.get( params.id )
//        if (session.user.id != todo.owner.id) {
//    		flash.message = "You can only delete your own Todo"
//    		redirect(action:list)
//        }
//        else {
//            if(todo) {
//                todo.delete()
//                flash.message = "Todo ${params.id} deleted."
//                redirect(action:list)
//            }
//            else {
//                flash.message = "Todo not found with id ${params.id}"
//                redirect(action:list)
//            }
//        }
//    }

    /**
     * Called when retrieving the todo to be edited.
     */
    def edit = {
        def todo = Todo.get( params.id )
        if (session.user.id != todo.owner.id) {
            flash.message = "You can only edit your own Todo"
            redirect(action:list)
            return
        }

        if(!todo) {
            flash.message = "Todo not found with id ${params.id}"
            redirect(action:list)
        }
        else {
          render(view:'list', model:[todo:todo, todoList:listByOwner()])
        }
    }

    /**
     * This will update the todo that was submitted.
     */
    def update = {

        def todo = Todo.get( params.id )

        if (session.user.id != todo.owner.id) {
          flash.message = "You can only update your own Todo"
          redirect(action:list)
        }
        else if(todo) {
          // save the updates to parameters
          todo.properties = params
          // upload the file if it is there
          uploadFileData(todo)
          if(todo.save()) {
            flash.message = "Todo ${params.name} updated."
            redirect(action:list)
          }
          else {
            // errors saving it
            render(view:'list', model:[todo:todo, todoList:listByOwner()])
          }
        }
        else {
            // not likely to be called unless you did something
            // illegal in accessing
            flash.message = "Todo not found with id ${params.id}"
            redirect(action:list)
        }
    }

    /**
     * This will save the todo along with a potentially upload file.
     * This will pass a message or error if it is created or not created
     * successfuly.
     */
    def save = {
	
	    println "JIM(todo): ${params}"
		
        def todo = new Todo()
        todo.properties = params
        
        // set some default objects
        def owner = User.get(session.user.id);
        todo.owner = owner
        todo.createdDate = new Date()
        todo.lastModifiedDate = new Date()

        // upload the file if it is there
        uploadFileData(todo)

        if(todo.save()) {
            flash.message = "Todo ${todo.name} created."
            //redirect(action:show,id:todo.id)
            redirect(action: list)
        }
        else {
            //flash.message =  "IT didnt work!!!"
            //render(view:'create',model:[todo:todo])
            todo.validate()
            render(view:'list', model:[todo:todo, todoList:listByOwner()])
            //, todoList:])
                //var: 'todo', collection:listByOwner())
        }
        // re render the page
        // use the following if you aren't gona do a file upload
        //render(template:'detail',  var: 'todo', collection:listByOwner())
        // redirect to our list        
    }

    /**
     * This will get the file from the multipart request
     * and save it to the todo.
     */
    def uploadFileData = { todo ->
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
            CommonsMultipartFile file = (CommonsMultipartFile)multiRequest.getFile("associatedFile");
            todo.fileName = file.originalFilename
            todo.contentType = file.contentType
            todo.associatedFile = file.bytes
        }
    }


    /**
     * This is used if you want to refresh the list.
     * via an ajax'ish way
     */
    private def listByOwner = {
        log.info("List")
        if(!params.max)params.max = 100
        def owner = User.get(session.user.id)
        //Todo.findAllByOwnerOrderByLastModifiedDate(owner, params)
        //Todo.findAllByOwner(owner, params, order: 'lastModifiedDate')
        // this needs to have an order by
        def list = Todo.findAllByOwner(owner, params)
        list
    }

    /**
     * This completes the task.
     * It will save the completed date and re render that section of the page.
     */
    def completeTask = {
        log.info "Complete Task"
        Todo t = Todo.get( params['id'] )
        // update the date now
        t.completedDate = new Date()
        t.lastModifiedDate = new Date()
        // model is for a specific name .... bean is for "it"
        render(template:'detail', model:[todo:t])
    }

    /**
     * Used to show thet note information.
     */
    def showNotes = {
        def note = Todo.executeQuery("select t.note From Todo as t Where t.id = ${params['id']}")
        // why am i having to do an array?
        render note[0]
    }

    /**
     * Used to remove the task from the page
     * We won't want to render anything on the page after.
     */
    def removeTask = {
        Todo t = Todo.get( params['id'] )
        log.info "Delete - $t"
        t.delete();
        // now lets just eliminate our item from the page.
        render ''
    }
	
	/**
	 * Used to retrieve user Todo for reports
	 */
	def userTodo = {
        	def user = User.get(session.user.id)
    		return Todo.findAllByOwner(user)
    }

}