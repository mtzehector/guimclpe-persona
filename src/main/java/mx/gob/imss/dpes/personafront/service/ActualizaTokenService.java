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
import mx.gob.imss.dpes.personafront.exception.ActualizaCorreoException;
import mx.gob.imss.dpes.personafront.exception.ActualizaTokenException;
import mx.gob.imss.dpes.personafront.model.ActualizaTokenRequest;
import mx.gob.imss.dpes.personafront.model.ActualizaTokenResponse;
import mx.gob.imss.dpes.personafront.model.ActualizacionDatosPensionado;
import mx.gob.imss.dpes.personafront.restclient.ActualizarTokenClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author juanf.barragan
 */
@Provider
public class ActualizaTokenService extends ServiceDefinition<ActualizacionDatosPensionado,ActualizacionDatosPensionado>{
    
    @Inject
    @RestClient
    private ActualizarTokenClient client;

    @Override
    public Message<ActualizacionDatosPensionado> execute(Message<ActualizacionDatosPensionado> request) throws BusinessException {
        
        //log.log(Level.INFO, "ActualizaTokenService execute");
        //log.log(Level.INFO, "Datos entrada {0}", request);
        try {
            if (request.getPayload().getPensionadoDatosNuevos().getCorreoElectronico() != null && !request.getPayload().getPensionadoDatosNuevos().getCorreoElectronico().isEmpty()) {

                ActualizaTokenRequest rq = new ActualizaTokenRequest();

                rq.setCorreo(request.getPayload().getPensionadoDatosNuevos().getCorreoElectronico());

                Response response = client.ActualizaToken(rq);

                if (!(response != null && response.getStatus() == 200))
                    throw new ActualizaCorreoException(ActualizaCorreoException.ERROR_ACTUALIZAR_TOKEN);
                    //log.log(Level.INFO, "Recuperacion de token correcta");

                ActualizaTokenResponse res = response.readEntity(ActualizaTokenResponse.class);

                request.getPayload().setToken(res.getToken());

                return request;
            }
            return request;
        } catch (Exception e) {
            log.log(Level.INFO, "ERROR::ActualizaTokenService.execute = {0}", e);
            throw new ActualizaCorreoException(ActualizaCorreoException.ERROR_DESCONOCIDO_DEL_SERVIDOR);
        }
    }
}
