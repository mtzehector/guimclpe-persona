/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.personafront.service;

import static java.lang.Math.log;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.PersonaByCurpExistException;
import mx.gob.imss.dpes.common.exception.PersonaByEmailExistException;
import mx.gob.imss.dpes.common.exception.PersonaEFByCurpExistException;
import mx.gob.imss.dpes.common.exception.UnknowException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.BaseService;
import mx.gob.imss.dpes.personafront.assembler.PersonaAssembler;
import mx.gob.imss.dpes.personafront.model.PersonaOrModel;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import mx.gob.imss.dpes.support.util.ExceptionUtils;
import mx.gob.imss.dpes.personafront.restclient.PersonaByCurpClient;

/**
 *
 * @author gabriel.rios
 */
@Provider
public class ValidateUserPersonaService extends BaseService {

    @Inject
    @RestClient
    private PersonaByCurpClient personaByCurp;
    @Inject
    private PersonaAssembler personaAssembler;



    
    public Integer validateUserPersona(Message<PersonaOrModel> request) throws BusinessException {
        log.log(Level.INFO, ">>>>>>>||||||||||||||||||||||||||||||||| personaFront ValidatePersonaService.validateUserPersona request.getPayload()= {0}", request.getPayload());
        int persona = 0;
        int personaCurp = 0;
        try{
            persona = personaByCurp.validateByEmail(request.getPayload().getPersona().getCorreoElectronico());
            personaCurp = personaByCurp.validateByCurp(request.getPayload().getPersona().getCveCurp());
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
        log.log(Level.INFO, ">>>>>>>personaFront ValidatePersonaService.validateUserPersona  Persona?= {0}", persona);

        if (persona == 1) {
           //Correo existe
            return 1;
        }
        if (personaCurp==1) {
            //Curp existe en Persona
            return 2;
            
        }
        if (personaCurp==2) {
            //Curp existe en PersonaEF
            return 3;
            
        }
        return 0;
       
        
    }
        
        

}
