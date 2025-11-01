/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.personafront.endpoint;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.PersonaNoRegistradaException;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.personafront.model.PersonasRQ;
import mx.gob.imss.dpes.personafront.service.ConsultarPromotorAsignarService;

/**
 *
 * @author juan.garfias
 */
@Path("/promotor")
@RequestScoped
public class PromotorEndPoint extends BaseGUIEndPoint<BaseModel, BaseModel, BaseModel>{

    @Inject
    private ConsultarPromotorAsignarService consultarPromotorService;
    protected final Logger log = Logger.getLogger(getClass().getName());

    @POST
    @Path("/promotoresValidos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response execute(PersonasRQ request) throws BusinessException {

        log.log(Level.INFO, "request= {0}", request);

        Message<PersonasRQ> response
                = consultarPromotorService.execute(new Message<>(request));

        return toResponse(response);

    }

}
