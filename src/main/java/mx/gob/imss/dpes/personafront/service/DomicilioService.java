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
import mx.gob.imss.dpes.interfaces.domicilio.model.Domicilio;
import mx.gob.imss.dpes.personafront.model.PersonaOrModel;
import mx.gob.imss.dpes.personafront.restclient.DomicilioClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class DomicilioService extends ServiceDefinition<PersonaOrModel, PersonaOrModel> {

    @Inject
    @RestClient
    private DomicilioClient client;

    @Override
    public Message<PersonaOrModel> execute(Message<PersonaOrModel> request) throws BusinessException {
        log.log(Level.INFO, ">>>DomicilioService Inicia Domicilio: {0}", request.getPayload().getPersona().getDomicilio());
        Response create = client.create(request.getPayload().getPersona().getDomicilio());
        if (create.getStatus() == 200) {
            Domicilio response = create.readEntity(Domicilio.class);
            request.getPayload().getPersona().setDomicilio(response);
            request.getPayload().getPersona().setCveRefDomicilio(response.getIdDomicilio());
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new PersonaNoRegistradaException(), null);
    }

    public Domicilio createOrUpdateDomicilio(Domicilio domicilio) throws BusinessException {
        log.log(Level.INFO, ">>> DomicilioService createDomicilio domicilio= {0}", domicilio);
        Response create = null;
        Domicilio response = null;
//if(false){
        if (domicilio.getIdDomicilio() != null && domicilio.getIdDomicilio().trim().length() > 0) {
            create = client.update(domicilio);
            if (create != null && create.getStatus() == 200) {
                response = domicilio;
                log.log(Level.INFO, ">>> DomicilioService updateDomicilio");
            }
            
        } else {
            create = client.create(domicilio);

            if (create != null && create.getStatus() == 200) {
                response = create.readEntity(Domicilio.class);
                log.log(Level.INFO, ">>> DomicilioService createDomicilio response= {0}", response);
            }
        }
        return response;
    }

    public Domicilio getDomicilio(String id) throws BusinessException {
        log.log(Level.INFO, ">>> DomicilioService getDomicilio id: {0}", id);
        Response create = null;
        Domicilio response = null;
        if (id != null && id.length() > 0) {
            Domicilio domicilio = new Domicilio();
            domicilio.setIdDomicilio(id);
            create = client.load(domicilio);
        }
        if (create != null && create.getStatus() == 200) {
            response = create.readEntity(Domicilio.class);
            log.log(Level.INFO, ">>> DomicilioService getDomicilio response: {0}", response);
        }
        return response;
    }

}
