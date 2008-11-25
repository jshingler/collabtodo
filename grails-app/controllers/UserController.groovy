
import groovy.text.SimpleTemplateEngine
import groovy.text.Template

import org.springframework.mail.MailException

import org.apache.commons.codec.digest.DigestUtils as DU

class UserController {

    EMailAuthenticatedService eMailAuthenticatedService
    
    def index = { 
        render(view: 'register')
    }

    def registration = {
        println "Register controller"    
    }


    def show = {
        [ user : User.get( params.id ) ]
    }

    def delete = {
        
        if (session.user.id != params.id) {
    		flash.message = "You can only delete yourself"
    		redirect(action:list)
    		return
        }
        
        def user = User.get( params.id )
        
        if(user) {
            user.delete()
            flash.message = "User ${params.id} deleted."
            redirect(action:list)
        }
        else {
            flash.message = "User not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        
        if (session.user.id != params.id) {
            flash.message = "You can only edit yourself"
            redirect(action:list)
            return
        }
        
        def user = User.get( params.id )
        
        if(!user) {
                flash.message = "User not found with id ${params.id}"
                redirect(action:list)
        }
        else {
            return [ user : user ]
        }
    }

    def update = {
        
        if (params.id != session.user.id) {
    		flash.message = "You can only update yourself"
    		redirect(action:list)
    		return
        }
        
        def user = User.get( params.id )
        
        if(user) {
             user.properties = params
            if(user.save()) {
                flash.message = "User ${params.id} updated."
                redirect(action:show,id:user.id)
            }
            else {
                render(view:'edit',model:[user:user])
            }
        }
        else {
            flash.message = "User not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def user = new User()
        user.properties = params
        return ['user':user]
    }

    def save = {
        def user = new User()
        user.properties = params
        if(user.save()) {
//            flash.message = "User ${user.id} created."
            flash.message = "user.saved.message"
            flash.args = [ user.firstName, user.lastName]
            flash.defaultMsg = "User Saved"
            redirect(action:show,id:user.id)
        }
        else {
            render(view:'create',model:[user:user])
        }
    }

    /**
     * This method is used to register a user.
     *
     */
    def handleRegistration = {
        def user = new User()
        log.info("HANDLE REGISTRATION")
        // using the log.info here will only print out the object, not a listing of it
        println params
        // create the property object
        user.properties = params
        // Process the captcha request
        if (params.captcha.toUpperCase() == session.captcha) {
          if(params.password != params.confirm) {
            flash.message = "The two passwords you entered dont match!"
            render(view:'register', model:[user:user])
          }
          else if (user.password?.length() <= 3) {
            flash.message = "The password length is under 3 characters."
            render(view:'register', model:[user:user])
          } else {
            log.info "before save"            
            // lets hash the password
            user.password = DU.md5Hex(user.password)

            if(user.save()) {
                // also add this user to the authority system
                def userAuth = Authority.findByAuthority("ROLE_USER")
                userAuth.addToPeople(user)
                // not sure if the save is necessary.
                userAuth.save()

                // send a confirmation email
                sendAcknowledgment (user)
                log.info "saved redirecting to user controller"

                // they will be prompted to login when they
                // go to an internal place anyway.
                redirect(controller:'todo')
            }
            else {
                log.info "didn't save"
                println "didn't save"
                flash.user = user
                render(view:'register', model:[user:user])
            }
          }
        }
        else {
            log.info "Captcha Not Filled In"
            flash.message = "Access code did not match."
            render(view: 'register', model:[user:user])
        }
    }

	/**
     * This method is used to find a user for Ajax returns.
     * The method will use the parameter userId and search for any
     * match in the userName, first name, or last name.
     *
     */
    def findUsers = {
        log.info "Find users"

        // lets query the database for any close matches to this
        def users = User.createCriteria().list {
            or {
                like("userName", "%${params.userId}%")
                like("firstName", "%${params.userId}%")
                like("lastName", "%${params.userId}%")
            }
            order("lastName")
            order("firstName")
        }

        // let's build our output XML
        def writer = new StringWriter()

        // build it
        new groovy.xml.MarkupBuilder(writer).ul {
            for (u in users) {
                li(id: u.id, "${u.lastName}, $u.firstName")
            }
        }

        render writer.toString()
    }

    private sendAcknowledgment  = { user ->

    // let's first design the email that we want to send
    def emailTpl = this.class.classLoader.getResource(
                        "web-app/WEB-INF/templates/regisrationEmail.gtpl")
        def binding = ["user": user]
        def engine = new SimpleTemplateEngine()
        def template = engine.createTemplate(emailTpl).make(binding)
        def body = template.toString()

// set up the email to send.
        def email = [
    to: [user.email],
                subject: "Your Collab-Todo Report",
                text: 	body
        ]

        try {
          eMailAuthenticatedService.sendEmail(email, [])
        } catch (MailException ex) {
                log.error("Failed to send emails", ex)
                return false
        }
        true
    }
}