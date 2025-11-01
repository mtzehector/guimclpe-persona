/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.personafront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.PersonaByCurpExistException;
import mx.gob.imss.dpes.common.exception.PersonaByEmailExistException;
import mx.gob.imss.dpes.common.exception.PersonaEFByCurpExistException;
import mx.gob.imss.dpes.common.exception.UnknowException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.personafront.assembler.PersonaAssembler;
import mx.gob.imss.dpes.personafront.model.PersonaOrModel;
import mx.gob.imss.dpes.support.util.ExceptionUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import mx.gob.imss.dpes.personafront.restclient.PersonaByCurpClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class ValidarAdminEFService extends ServiceDefinition<PersonaOrModel, PersonaOrModel>{
    
    @Inject
    @RestClient
    private PersonaByCurpClient personaByCurp;
    @Inject
    private PersonaAssembler personaAssembler;



    @Override
    public Message<PersonaOrModel> execute(Message<PersonaOrModel> request) throws BusinessException {
        
        int persona = 0;
        int personaCurp = 0;
        try{
            if(request.getPayload().getPersona().getCveCurp() != null ){
                personaCurp = personaByCurp.validateByCurp(request.getPayload().getPersona().getCveCurp());
            }else if(request.getPayload().getPersona().getCorreoElectronico() != null){
                persona = personaByCurp.validateByEmail(request.getPayload().getPersona().getCorreoElectronico());
            }
            
            
        } 
        
        catch (RuntimeException e) {
            e.printStackTrace();
            log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR: RuntimeException message=\n{0}", e.getMessage());
            if (e.getMessage().contains("Unknown error, status code 404") || e.getMessage().contains("Unknown error, status code 502")) {
                log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR: Servicio no encontrado {0}", "RegistroPensionadoBack");
                ExceptionUtils.throwServiceException("RegistroPensionadoBack");
            }
            throw new UnknowException();
        }
        log.log(Level.INFO, ">>>>>>>personaFront ValidatePersonaService.execute  Persona?= {0}", persona);

        if (persona == 1) {
           throw new PersonaByEmailExistException();
        }
        if (personaCurp==1) {
            throw new PersonaByCurpExistException();
        }
        if (personaCurp==2) {
            throw new PersonaEFByCurpExistException();
        }
        return new Message<>(request.getPayload());
}
}
