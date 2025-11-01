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
import mx.gob.imss.dpes.common.exception.EMailExistException;
import mx.gob.imss.dpes.common.exception.UnknowException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.RegistroRequest;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.personafront.model.PersonaOrModel;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import mx.gob.imss.dpes.personafront.restclient.RegistroBackClient;
import mx.gob.imss.dpes.support.util.ExceptionUtils;

/**
 *
 * @author gabriel.rios
 */
@Provider
public class ValidateUserRegistroService extends ServiceDefinition<PersonaOrModel, PersonaOrModel> {

    @Inject
    @RestClient
    private RegistroBackClient client;



    @Override
    public Message<PersonaOrModel> execute(Message<PersonaOrModel> request) throws BusinessException {
        log.log(Level.INFO, ">>>>>>>||||||||||||||||||||||||||||||||| personaFront ValidateUserRegistroService.execute request.getPayload()= {0}", request.getPayload());
        int usuario = 0;
        try{
            Response response;
            // usuario = client.validateUser(request.getPayload().getRegistroRequest());
            response = client.validateUser(request.getPayload().getRegistroRequest());
            if (response != null && response.getStatus() == 200)
                usuario = response.readEntity(Integer.class);
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR: RuntimeException message=\n{0}", e.getMessage());
            if (e.getMessage().contains("Unknown error, status code 404") || e.getMessage().contains("Unknown error, status code 502")) {
                log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR: Servicio no encontrado {0}", "RegistroPensionadoBack");
                ExceptionUtils.throwServiceException("RegistroPensionadoBack");
            }
            throw new UnknowException();
        }
        log.log(Level.INFO, ">>>>>>>personaFront ValidateUserRegistroService.execute  Usuario?= {0}", usuario);

        if (usuario == 1) {
           log.log(Level.INFO, ">>>>>>>personaFront ValidateUserRegistroService.execute  Usuario?= {0}", usuario);
            throw new EMailExistException();
        }
        
        return new Message<>(request.getPayload());
       
        
    }
    
    
           
        

}
