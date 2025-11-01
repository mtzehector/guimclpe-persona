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
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.personafront.model.PersonaOrModel;
import mx.gob.imss.dpes.personafront.model.PersonaRequest;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/persona")
@RegisterRestClient
public interface PersonaByCurpClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{curp}")
    public Response load(@PathParam("curp") String curp);
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/email/{email}")
    public Integer validateByEmail(@PathParam("email") String email);
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/curp/{curp}")
    public Integer validateByCurp(@PathParam("curp") String curp);
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{id}")
    public Response getPersonaById(@PathParam("id") Long id);
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/rp/{rp}/{ef}")
    public Integer validateByRP(@PathParam("rp") String rp,@PathParam("ef") String ef);
    

}
