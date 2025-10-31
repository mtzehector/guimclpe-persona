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
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.personafront.model.BitacoraPersonaRq;
import mx.gob.imss.dpes.personafront.model.PersonaOrModel;
import mx.gob.imss.dpes.personafront.restclient.BitacoraBackClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author juanf.barragan
 */
@Provider
public class BitacoraBackService extends ServiceDefinition<PersonaOrModel, PersonaOrModel> {

    @Inject
    @RestClient
    private BitacoraBackClient client;
    
    @Override
    public Message<PersonaOrModel> execute(Message<PersonaOrModel> request) throws BusinessException {
        log.log(Level.INFO, "Inicia BitacoraBackService ");
        if(request.getPayload().getRegistroBitacora() != null){
            BitacoraPersonaRq bitacoraRQ = new BitacoraPersonaRq();
            bitacoraRQ.setActividad("1");
            bitacoraRQ.setMotivo("4");
            bitacoraRQ.setCvePersona(request.getPayload().getPersona().getId());
            bitacoraRQ.setCvePersonalEF(request.getPayload().getPersona().getCvePersonalEf());
            bitacoraRQ.setCveRegistroPromotor(request.getPayload().getPersona().getCvePersonalEf());
            
            Response res = client.create(bitacoraRQ);
            
            if(res.getStatus() == 200){
                log.log(Level.INFO, "Registro en Bitacora Persona Exitoso", bitacoraRQ);
            }
            else{
                log.log(Level.INFO, "Registro en Bitacora Persona NO Exitoso", bitacoraRQ);
            }
            
        }
        
        
        return new Message<>(request.getPayload());
    }
    
}
