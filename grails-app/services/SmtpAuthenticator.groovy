

import javax.mail.Authenticator

class SmtpAuthenticator extends Authenticator {

	private String username;
    private String password;

    public SmtpAuthenticator(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    public javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(username, password);
    }

}