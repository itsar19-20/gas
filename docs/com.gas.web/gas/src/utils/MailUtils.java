package utils;

import java.util.Properties;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtils {
	public String sendNewPass(String email) {
		String newPass = UUID.randomUUID().toString().replace("-", "");
		newPass = newPass.substring(0, Math.min(newPass.length(), 10));

		String FROM = "loscoiattolodellabenzina@gmail.com";
		String FROMNAME = "ses-smtp-user.20200316-222900";
		String myPass = "G454DV1S0R";
		String SMTP_USERNAME = "AKIAUMAHUHQRIYF2B56A";
		String SMTP_PASSWORD = "BOSq6ld9CqwST/hc2W5MmTnzRvcOxSmmCPbe0dCarxTi";
		String HOST = "email.eu-central-1.amazonaws.com";
		String SUBJECT = "GasAdvisor Password Recovery";
		String BODY = String.join(System.getProperty("line.separator"), "<h1>GasAdvisor Password Recovery:</h1>",
				"<p>Il suo nuovo password e' " + newPass + " </p>");
		String TO = email;
		int PORT = 587;
		Properties props = System.getProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.port", PORT);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(props);
		Message msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(FROM, FROMNAME));
			System.out.println("1");
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
			System.out.println("2");
			msg.setSubject(SUBJECT);
			System.out.println("3");
			msg.setContent(BODY, "text/html");
			Transport transport = session.getTransport();
			System.out.println("4");
			transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
			System.out.println("5");
			transport.sendMessage(msg, msg.getAllRecipients());
			System.out.println("6");
			transport.close();
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			return null;
		}
		return newPass;

	}

}
