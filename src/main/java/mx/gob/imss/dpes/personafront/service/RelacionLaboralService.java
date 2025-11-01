/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.personafront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.PersonaNoRegistradaException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.relacionlaboral.model.RelacionLaboralOut;
import mx.gob.imss.dpes.personafront.model.PersonaOrModel;
import mx.gob.imss.dpes.personafront.restclient.RelacionLaboralClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class RelacionLaboralService extends ServiceDefinition<PersonaOrModel, PersonaOrModel> {

    @Inject
    @RestClient
    private RelacionLaboralClient client;

    @Override
    public Message<PersonaOrModel> execute(Message<PersonaOrModel> request) throws BusinessException {
        log.log(Level.INFO, ">>> RelacionLaboralService.execute request.getPayload().getLaboralRequest()= {0}", request.getPayload().getLaboralRequest());
        Response create = client.load(request.getPayload().getLaboralRequest());
        log.log(Level.INFO, ">>> RelacionLaboralServic create.getStatus(): {0}", create.getStatus());
        if (create.getStatus() == 200) {
            RelacionLaboralOut response = create.readEntity(RelacionLaboralOut.class);
            if (response.getCode().equals("000")) {
                log.log(Level.INFO, ">>> RelacionLaboralService !!!OK response={0}", response);
                request.getPayload().setLaboralResponse(response);
                return new Message<>(request.getPayload());
            }
            log.log(Level.SEVERE, ">>> ERROR! RelacionLaboralService.execute response= {0}", response);
            throw new PersonaNoRegistradaException(PersonaNoRegistradaException.PATRON);

        }
        return response(null, ServiceStatusEnum.EXCEPCION, new PersonaNoRegistradaException(), null);
    }

}
