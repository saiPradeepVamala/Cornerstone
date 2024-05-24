package com.cornerstonehospice.android.utils;


/**
 * 
 */

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author Shashi
 *
 */
public class EmailUtils {

	public static boolean sentEmail(String messageBody) {
		java.util.Properties props = new java.util.Properties();

		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com"); // "smtp.gmail.com"
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port",  "465");//587);

		try {
			// Get the Session object.
			Session session = Session.getInstance(props,
					new javax.mail.Authenticator() {
						protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
							return new javax.mail.PasswordAuthentication("wenabledevtest1@gmail.com", "fwvnzhjznmkwimaj");
						}
					});

			if (session != null) {
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress("wenabledevtest1@gmail.com"));//From
				message.addRecipient(Message.RecipientType.TO,
						new InternetAddress("wenabledevtest1@gmail.com"));  //TO
				message.setHeader("MIME-Version", "1.0");
				message.setHeader("Content-Type", "text/plain; charset=us-ascii");
				message.setSubject("Test subject");
				message.setContent("test content", "text/html");
				Transport.send(message);
			}

		} catch (Exception e) {
		}
		return true;
	}
}
