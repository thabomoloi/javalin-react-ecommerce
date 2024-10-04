package com.oasisnourish.services.impl;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;
import java.util.Properties;

import com.oasisnourish.services.EmailService;
import com.oasisnourish.config.EnvironmentConfig;

public class EmailServiceImpl implements EmailService {
    private final TemplateEngine templateEngine;
    private final Dotenv dotenv;
    private final String MAIL_USERNAME;
    private final String MAIL_PASSWORD;

    public EmailServiceImpl(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        dotenv = EnvironmentConfig.getDotenv();
        MAIL_PASSWORD = dotenv.get("MAIL_PASSWORD");
        MAIL_USERNAME = dotenv.get("MAIL_USERNAME");
        if (MAIL_PASSWORD == null || MAIL_USERNAME == null) {
            throw new IllegalStateException("Mail environment variables are not set.");
        }
    }

    @Override
    public void sendEmail(String to, String subject, String templateName, IContext context) {
        // Set up mail server properties
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props);

        try {
            // Create a MimeMessage
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            // Process the Thymeleaf template
            String htmlContent = templateEngine.process(templateName, context);

            // Set the content of the email
            message.setContent(htmlContent, "text/html");

            // Send the message
            Transport.send(message, MAIL_USERNAME, MAIL_PASSWORD);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
