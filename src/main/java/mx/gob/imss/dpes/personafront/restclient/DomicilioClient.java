/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.personafront.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.interfaces.domicilio.model.Domicilio;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author eduardo.loyo
 */
@Path("/domicilio")
@RegisterRestClient
public interface DomicilioClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Cliente Servicios digitales domicilio",
            description = "Obtener el id de la persistencia de los datos de domicilio")
    public Response create(Domicilio request);
    
    @PUT
    @Path("/actualizar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Cliente Servicios digitales domicilio",
            description = "Obtener el id de la persistencia de los datos de domicilio")
    public Response update(Domicilio request);

    @POST
    @Path("/obtener")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Cliente Servicios digitales domicilio",
            description = "Obtener el id de la persistencia de los datos de domicilio creado")
    public Response load(Domicilio request);

}
