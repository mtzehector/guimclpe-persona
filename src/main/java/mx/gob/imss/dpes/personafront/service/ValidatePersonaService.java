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
import mx.gob.imss.dpes.common.exception.PersonaByCurpExistException;
import mx.gob.imss.dpes.common.exception.PersonaByEmailExistException;
import mx.gob.imss.dpes.common.exception.PersonaEFByCurpExistException;
import mx.gob.imss.dpes.common.exception.RegistroPatronalNoExisteException;
import mx.gob.imss.dpes.common.exception.UnknowException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.PersonaP;
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
public class ValidatePersonaService extends ServiceDefinition<PersonaOrModel, PersonaOrModel> {

    @Inject
    @RestClient
    private PersonaByCurpClient personaLoadClient;
    @Inject
    private PersonaAssembler personaAssembler;

    @Override
    public Message<PersonaOrModel> execute(Message<PersonaOrModel> request) throws BusinessException {
        log.log(Level.INFO, ">>>>>>>||||||||||||||||||||||||||||||||| personaFront ValidatePersonaService.execute request.getPayload()= {0}", request.getPayload());
        int persona = 0;
        int personaCurp = 0;
        int personaRP = 0;
        try {
            persona = personaLoadClient.validateByEmail(request.getPayload().getPersona().getCorreoElectronico());
            personaCurp = personaLoadClient.validateByCurp(request.getPayload().getPersona().getCveCurp());
            if(request.getPayload().getPersona().getRegistroPatronal() != null && !request.getPayload().getPersona().getRegistroPatronal().equals(""))
                personaRP = personaLoadClient.validateByRP(request.getPayload().getPersona().getRegistroPatronal(),request.getPayload().getPersona().getCveEntidadFinanciera().toString());
        } catch (RuntimeException e) {
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
        if (personaCurp == 1) {
            throw new PersonaByCurpExistException();
        }
        if (personaCurp == 2) {
            throw new PersonaEFByCurpExistException();
        }
        if (personaRP == 3){
            throw new RegistroPatronalNoExisteException();
        }
        if(personaCurp != 0){
            request.getPayload().getPersona().setId(Long.parseLong(personaCurp+""));
            request.getPayload().setRegistroBitacora("1");
        }
        return new Message<>(request.getPayload());

    }

    public int isNewPersona(PersonaOrModel request) throws BusinessException {
        log.log(Level.INFO, ">>>>>>>||| personaFront ValidatePersonaService.isNewPersona request= {0}", request);
        Integer usuario = 0;
        Response response = personaLoadClient.getPersonaById(request.getPersona().getId());
        if (response.getStatus() == 200) {
            boolean isPersonaChange = false;
            PersonaP persona = response.readEntity(PersonaP.class);
            PersonaP newPersona = request.getPersona();
            if (persona != null) {
                String newEmail = newPersona.getCorreoElectronico();
                String originalEMail = persona.getCorreoElectronico();
                if (persona.getCveTipoEmpleado().compareTo(newPersona.getCveTipoEmpleado()) != 0) {
                    setDataForPersonaChange(request);
                    isPersonaChange = true;
                    usuario = 2;
                }
                if (originalEMail.trim().compareTo(newEmail.trim()) != 0) {
                    if(isPersonaChange){
                        usuario = 3;
                    }
                    else{
                        usuario = 1;
                    }
                }

            }
        }
        log.log(Level.INFO, ">>>>>>>||| personaFront ValidatePersonaService.isNewPersona usuario= {0}", usuario);
        return usuario;

    }

   protected void setDataForPersonaChange(PersonaOrModel request) {
        if (request.getPersona().getCveTipoEmpleado().compareTo(PersistePersonaService.CVE_TIPO_PERSONA_EMPLEADO_TERCERO) == 0) {
            request.getPersona().setNumEmpleado(null);
        } else {//Third party employee ==> EF employee
            request.getPersona().setCveRefDomicilio(null);
        }
    }

}
