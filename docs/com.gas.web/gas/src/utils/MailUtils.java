package utils;

import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtils {
	public String sendNewPass(String email) {
		String newPass = UUID.randomUUID().toString().replace("-", "");
		newPass = newPass.substring(0, Math.min(newPass.length(), 10));
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");

		String myAccount = "loscoiattolodellabenzina@gmail.com";
		String myPass = "G454DV1S0R";

		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(myAccount, myPass);
			}
		});
		Message message = prepareMessage(session, myAccount, email, newPass);
		try {
			Transport.send(message);
			return newPass;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Message prepareMessage(Session session, String myAccount, String email, String newPass) {
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(myAccount));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
			message.setSubject("Email Recovery");
			message.setText("Il suo nuovo passord e': " + newPass);
			return message;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


}