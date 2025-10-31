/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.personafront.restclient;

/**
 *
 * @author antonio
 */
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/obtenerentidadfinanciera")
@RegisterRestClient
public interface EntidadFinancieraClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{regPatronal}")
    public Response load(@PathParam("regPatronal") String regPatronal);
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/asociados/{regPatronal}")
    public Response loadAso(@PathParam("regPatronal") String regPatronal);
    

}
