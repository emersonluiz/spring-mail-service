package br.com.emerson.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.inject.Inject;
import javax.mail.util.ByteArrayDataSource;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.emerson.file.FileAttachment;
import br.com.emerson.mail.Message;
import br.com.emerson.mail.SimpleMail;

public class DefaultEmailService implements EmailService {

    private SimpleMail simpleMail;
    private static final Logger logger = LoggerFactory.getLogger(DefaultEmailService.class);

    @Inject
    public DefaultEmailService(SimpleMail simpleMail) {
        this.simpleMail = simpleMail;
    }

    @Override
    public Response sendHello() {
        try {
            Message message = new Message();
            String from = "test@test.com.br";
            String[] to = {"test@test.com.br"};
            message.setFrom(from);
            message.setTo(to);
            message.setSubject("Hello World");
            message.setText("Hello my friend, this is my first exemple of the mail sender!!!");
            simpleMail.send(message);
            logger.info("Sending...");
            return Response.ok().build();
        } catch (Exception e) {
            logger.error("Error on sending mail!");
            return Response.serverError().build();
        }
    }

    @Override
    public Response send(MultipartBody body) {
        try {
            Message message = null;
            Attachment document = body.getAttachment("model");
            List<Attachment> list = body.getAllAttachments();

            if (document == null) {
                logger.error("document multipart (Model) [name=metadata] missing");
                return Response.serverError().build();
            }

            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(document.getDataHandler().getInputStream(), Message.class);

            if (message == null) {
                logger.error("document multipart (Message) [name=metadata] missing");
                return Response.serverError().build();
            }

            Map<String, ByteArrayDataSource> map = new HashMap<String, ByteArrayDataSource>();

            FileAttachment fa = new FileAttachment();

            for (Attachment item : list) {
                DataHandler dataHandler = item.getDataHandler();
                String name = dataHandler.getDataSource().getName();
                String type = dataHandler.getContentType();
                String fileName = item.getContentDisposition().getParameter("filename");
                if (!name.equals("model")) {
                    if (name != null && type != null && fileName != null) {
                        ByteArrayDataSource bds = fa.generate(type, dataHandler.getInputStream());
                        map.put(fileName, bds);
                    }
                }
            }

            String from = "test@test.com.br";
            message.setFrom(from);
            simpleMail.send(message, map);
            logger.info("Sending...");
            return Response.ok().build();
        } catch (Exception e) {
            logger.error("Error on sending mail!");
            return Response.serverError().build();
        }
    }

}
