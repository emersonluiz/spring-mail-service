package br.com.emerson.file;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.mail.util.ByteArrayDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileAttachment {

    private static final Logger logger = LoggerFactory.getLogger(FileAttachment.class);

    public ByteArrayDataSource generate(String type, InputStream is) throws IOException {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            byte[] byteArray = buffer.toByteArray();

            ByteArrayDataSource b = new ByteArrayDataSource(byteArray, type);
            return b;
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
