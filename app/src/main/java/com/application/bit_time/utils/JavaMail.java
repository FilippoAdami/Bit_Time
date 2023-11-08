package com.application.bit_time.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class JavaMail {
    private static final String SMTP_HOST = "mail.gmx.com";
    private static final String SMTP_PORT = "587";
    private static final String SMTP_USER = "bit.time@gmx.com";
    private static final String SMTP_PASSWORD = "NLCVNNNLYB2XJQBCJQ7Y";

    public static void sendEmail(final String to, final String subject, final String message) {
        Executor executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try {
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmx.com");
                props.put("mail.smtp.port", "587");

                Session session = Session.getInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(SMTP_USER, SMTP_PASSWORD);
                    }
                });

                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(SMTP_USER));
                msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
                msg.setSubject(subject);
                msg.setText(message);

                Transport.send(msg);
            } catch (Exception e) {
                e.printStackTrace();
                // Handle email sending errors here
            }
        });
    }
}


