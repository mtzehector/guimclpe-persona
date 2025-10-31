/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.personafront.service;

import java.util.Objects;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.personafront.model.ActualizacionDatosPensionado;
import mx.gob.imss.dpes.personafront.model.HistoricoUsuarioRequest;
import mx.gob.imss.dpes.personafront.restclient.HistoricoUsuarioClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author juanf.barragan
 */
@Provider
public class RegistroHistoricoUsuarioService extends ServiceDefinition<ActualizacionDatosPensionado,ActualizacionDatosPensionado>{

    
    @Inject
    @RestClient
    private HistoricoUsuarioClient client;
    
    @Override
    public Message<ActualizacionDatosPensionado> execute(Message<ActualizacionDatosPensionado> request) throws BusinessException {
        log.log(Level.INFO, "RegistroHistoricoUsuarioService execute");
        log.log(Level.INFO, "Datos entrada {0}", request);
        HistoricoUsuarioRequest rq = new HistoricoUsuarioRequest();
        Long persona = Long.parseLong(request.getPayload().getPensionadoDatosAnteriores().getCvePersona());
        rq.setCurpModificador(request.getPayload().getCurp());
        rq.setCvePersona(persona);
        rq.setCorreoElectronico(request.getPayload().getPensionadoDatosAnteriores().getCorreoElectronico() == null ? null : request.getPayload().getPensionadoDatosAnteriores().getCorreoElectronico());
        rq.setTelCelular((request.getPayload().getPensionadoDatosAnteriores().getTelCelular() == null || request.getPayload().getPensionadoDatosAnteriores().getTelCelular() == "") ? null : Long.parseLong(request.getPayload().getPensionadoDatosAnteriores().getTelCelular()));
        rq.setTelLocal((request.getPayload().getPensionadoDatosAnteriores().getTelLocal()== null || request.getPayload().getPensionadoDatosAnteriores().getTelLocal() =="")? null : Long.parseLong(request.getPayload().getPensionadoDatosAnteriores().getTelLocal()));
        Response response = client.RegistraHistorico(rq);
        
        if(response.getStatus() == 200 || response.getStatus() == 204){
            log.log(Level.INFO, "Guardado correcto en Historico Usuario {0}", rq );
            return request;
        }
        log.log(Level.INFO, "Error en el guardado en historico usuario {0}", rq);
        return request;
    }
    
    
    
}
