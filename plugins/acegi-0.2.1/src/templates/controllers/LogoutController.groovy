/**
 * Logout Controller (Example)
 * @author generated by plugin script
 */
class LogoutController {

  def index = {
    /* ---- put your codes here ----  */
    
    redirect(uri:"/j_acegi_logout")
    render(text:"")
  }
}
