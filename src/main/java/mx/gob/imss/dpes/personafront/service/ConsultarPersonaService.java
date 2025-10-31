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
import mx.gob.imss.dpes.common.exception.UnknowException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Persona;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.PersonaP;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.PersonaResponse;
import mx.gob.imss.dpes.personafront.assembler.PersonaAssembler;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import mx.gob.imss.dpes.personafront.restclient.PersonaByCurpClient;

/**
 *
 * @author antonio
 */
@Provider
public class ConsultarPersonaService extends ServiceDefinition<Persona, PersonaResponse> {

    @Inject
    @RestClient
    private PersonaByCurpClient personaByCurp;
    @Inject
    private PersonaAssembler personaAssembler;



    @Override
    public Message<PersonaResponse> execute(Message<Persona> request) throws BusinessException {
        // Se busca en el sistema de enrolamiento y despues de nuestra base
        log.log(Level.INFO, "ConsultarPersonaService.execute request= {0}", request);
        Response load = null;
        try{
            load = personaByCurp.load(request.getPayload().getCurp());
        }
        catch(Exception e){
            e.printStackTrace();
            throw new UnknowException();
        }
        if (load.getStatus() == 200) {
            PersonaP source = load.readEntity(PersonaP.class);
            PersonaResponse respuesta = personaAssembler.assemble(source);
            return new Message<>(respuesta);
        }
        

        return response(null, ServiceStatusEnum.EXCEPCION, new PersonaNoRegistradaException(), null);

    }

}
