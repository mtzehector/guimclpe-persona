package mx.gob.imss.dpes.personafront.restclient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import mx.gob.imss.dpes.common.constants.Constantes;
import mx.gob.imss.dpes.personafront.model.ActualizacionDatosPensionado;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * Cliente Persona Back Servicios de Pensionado
 * @author luisr.rodriguez
 */
@Path("/pensionado")
@RegisterRestClient
public interface PensionadoClient {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getByCurpOrNss/{curp}/{nss}")
    public Response getByCurpOrNss(@PathParam("curp") String curp,
            @PathParam("nss") String nss);
    
    @PUT
    @Path("/actualizarDatosContacto")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDatosContacto(@HeaderParam(Constantes.HEADER_AUTHORIZATION) String header,
        ActualizacionDatosPensionado pensionado);
}