/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.personafront.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.PersonaP;
import mx.gob.imss.dpes.personafront.model.BajaOperadorRQ;
import mx.gob.imss.dpes.personafront.model.BajaPromotorRQ;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author eduardo.loyo
 */
@Path("/persona")
@RegisterRestClient
public interface PersonaPersistClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(PersonaP persona);

    @GET
    @Path("/promotoresValidos/{cveDelegacion}/{cveEntidadFinanciera}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response promotoresParaAsignar(
            @PathParam("cveDelegacion") Long cveDelegacion,
            @PathParam("cveEntidadFinanciera") Long cveEntidadFinanciera);

    @POST
    @Path("/delegaciones/{cvePersonalEF}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delegacionesPorPersona(
            @PathParam("cvePersonalEF") Long cvePersonalEF);

    @POST
    @Path("/bajaPromotor")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response bajaPromotor(
            BajaPromotorRQ promotor);
    
    @DELETE
    @Path("/bajaOperador")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response bajaOperador(BajaOperadorRQ promotor);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(PersonaP persona);

    @GET
    @Path("/id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response personaById(
            @PathParam("id") Long cvePersona);
}
