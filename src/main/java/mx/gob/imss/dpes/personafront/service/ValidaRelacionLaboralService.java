/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.personafront.service;

import java.util.logging.Level;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.PersonaNoRegistradaException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.relacionlaboral.model.InfoCIRelacionLaboral;
import mx.gob.imss.dpes.personafront.model.PersonaOrModel;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class ValidaRelacionLaboralService extends ServiceDefinition<PersonaOrModel, PersonaOrModel> {

    

    @Override
    public Message<PersonaOrModel> execute(Message<PersonaOrModel> request) throws BusinessException {
        log.log(Level.INFO,"---------Valida Registro Patronal-----");
        log.log(Level.INFO,">>> ValidaRelacionLaboralService.execute  request.getPayload().getLaboralRequest()={0}", request.getPayload().getLaboralRequest());
        String registroPatronal = request.getPayload().getPersona().getRegistroPatronal();
        
        
        boolean flagRL= false;
        for( InfoCIRelacionLaboral lstRL :request.getPayload().getLaboralResponse().getListInfoRelacionesLaborales()){
            if (lstRL.getLstInfoRelacionesLaborales().getFecFinRelLab().equals("9999-12-31")) {
                if (registroPatronal.equals(lstRL.getLstInfoRelacionesLaborales().getRegPatron())) {
                    flagRL=true;
                }
            }
        }
        
        /*
        
        if (request.getPayload().getLaboralResponse()
                .getListInfoRelacionesLaborales().get(0)
                .getLstInfoRelacionesLaborales().getRegPatron().equals(registroPatronal)) {
            log.log(Level.INFO,">>> ValidaRelacionLaboralService.execute !!!!!OK Coincide.  registroPatronal={0}", registroPatronal);
        
        */
        if (flagRL){
            return request;
        } else {
            throw new PersonaNoRegistradaException(PersonaNoRegistradaException.REG);
        }

    }

}
