package nl.ts.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class Hello {

    // This method is called if TEXT_PLAIN is request
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/simple/{idee}")
    public String sayPlainTextHello(@PathParam("idee") String id ) {
        return "Hello Jersey simple"  + " " + id;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/extra/{idee}/{twee}")
    public String sayTextHelloWithId(@PathParam("idee") String id, @PathParam("twee") String twee ) {
        return "Hello Jersey extra" + " " + id;
    }
}
