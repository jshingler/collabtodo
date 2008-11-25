
class UserInfoController {
    def index = { redirect(action:show,params:params) }

    def show = {
		def result = session.user
		format(result)
	}

	def beforeInterceptor = {

		def authHeader = request.getHeader("Authorization")
		if (authHeader) {
			def tokens = authHeader.split(' ')
			def user_password = tokens[1]
			tokens = user_password.split(':')
			def userid = tokens[0]
			def password = tokens[1]
			// At this point, the userid and password could be verified
			// to make sure that the person making the request is authenticated
			//
			// << AUTHENTICATION LOGIC / CALL >>
			//
			// Put look up the user object and put it into session for use
			// later by the controllers.
			def user = User.findByUserName(userid)
			if (user) {
			   session.user = user
			} else {
			   session.user = null
			}
		}
	}

	private format(obj) {
		def restType = (params.rest == "rest")?"XML":"JSON"
		println obj."encodeAs$restType"()
		render obj."encodeAs$restType"()
	}

}