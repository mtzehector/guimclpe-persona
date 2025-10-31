/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.personafront.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import mx.gob.imss.dpes.common.model.RegistroRequest;
import mx.gob.imss.dpes.personafront.model.UsuarioRQ;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author eduardo.loyo
 */
@Path("/registroPensionado")
@RegisterRestClient
public interface RegistroBackClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/usuario")
    @Operation(summary = "Creacion de Registro",
            description = "Creacion de registro de usuario para login")
    public Response validateUser(RegistroRequest request);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/updateIndActivo")
    public Integer updateIndActivo(UsuarioRQ request);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/updateCorreo")
    public Integer updateNomUsuario(UsuarioRQ request);

    @GET
    @Path("/{idPersona}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response obtenerUsuarioById(@PathParam("idPersona") Long id);

    @GET
    @Path("/usuario/{idPersona}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response obtenerUsuarioByIdPersona(@PathParam("idPersona") Long id);
}
