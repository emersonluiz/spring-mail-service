package br.com.emerson.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

@Path("/email")
public interface EmailService {

    @GET
    @Path("/hello")
    public Response sendHello();

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response send(MultipartBody body);
}
