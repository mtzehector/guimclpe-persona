package mx.gob.imss.dpes.personafront.restclient;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.interfaces.bitacora.model.BitacoraPensionadoRequest;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/bitacoraPensionado")
@RegisterRestClient
public interface BitacoraPensionadoClient {

    @POST
    @Path("/registrosActualizados")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response guardarRegistrosActualizados(List<BitacoraPensionadoRequest> mcltBitacoraPensionados) throws BusinessException;

}
