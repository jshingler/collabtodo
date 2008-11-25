import org.acegisecurity.context.SecurityContextHolder as SCH

/**
 * Created by IntelliJ IDEA.
 * User: joseph
 * Date: Dec 3, 2007
 * Time: 8:55:19 PM
 * To change this template use File | Settings | File Templates.
 */

class LoginFilters {

   def filters = {
        acegiLogin(uri:'*/j_acegi_security_check/*') {
            // println "Acegi Login Filter"

            def auth = SCH?.context?.authentication
            if (auth?.authenticated) {
                def userName = auth?.principal?.getUsername()
                session?.user = User.findByUserName(userName);
            }
        }

        userCreateFilter(controller:'*', action:'*') {
              before = {
                // println "Todo Filter"
                // temporay
                def auth = SCH?.context?.authentication
                // ROLE_ANONYMOUS
                if (auth?.authenticated && auth?.principal != 'anonymousUser') {
                    def userName = auth?.principal?.getUsername()
                    session?.user = User.findByUserName(userName);
                }
              }
        }
   }
}