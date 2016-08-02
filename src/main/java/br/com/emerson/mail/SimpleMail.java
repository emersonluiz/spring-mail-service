package br.com.emerson.mail;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

@Named
public class SimpleMail {

    private JavaMailSenderImpl mailSender;
    private SimpleMailMessage templateMessage;
    private static final Logger logger = LoggerFactory.getLogger(SimpleMail.class);

    @Inject
    public SimpleMail(JavaMailSenderImpl mailSender, SimpleMailMessage templateMessage) {
        this.mailSender = mailSender;
        this.templateMessage = templateMessage;
    }

    public void send(Message message) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
            if (message.getFrom() != null) {
                msg.setFrom(message.getFrom());
            }
            if (message.getCc() != null) {
                msg.setCc(message.getCc());
            }
            msg.setTo(message.getTo());
            msg.setSubject(message.getSubject());
            msg.setText(message.getText());

            this.mailSender.send(msg);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    public void send(Message message, Map<String, ByteArrayDataSource> files) throws Exception {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            if (message.getFrom() != null) {
                helper.setFrom(message.getFrom());
            }
            if (message.getCc() != null) {
                helper.setCc(message.getCc());
            }
            helper.setTo(message.getTo());
            helper.setSubject(message.getSubject());
            helper.setText("<html><body>" + message.getText() + "</body></html>", true);

            for (Map.Entry<String,ByteArrayDataSource> map : files.entrySet()) {
                helper.addAttachment(map.getKey(), map.getValue());
            }

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

}
