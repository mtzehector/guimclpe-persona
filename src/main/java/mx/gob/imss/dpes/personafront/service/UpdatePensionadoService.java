package mx.gob.imss.dpes.personafront.service;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import mx.gob.imss.dpes.common.constants.Constantes;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.EMailExistException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.RegistroRequest;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.personafront.exception.PersonaPensionadoException;
import mx.gob.imss.dpes.personafront.model.ActualizacionDatosPensionado;
import mx.gob.imss.dpes.personafront.restclient.PensionadoClient;
import mx.gob.imss.dpes.personafront.restclient.PersonaByCurpClient;
import mx.gob.imss.dpes.personafront.restclient.RegistroBackClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.logging.Level;

/**
 * @author luisr.rodriguez
 */
@Provider
public class UpdatePensionadoService extends ServiceDefinition<ActualizacionDatosPensionado, ActualizacionDatosPensionado> {
    @Inject
    @RestClient
    private PensionadoClient pensionadoClient;
    @Inject
    @RestClient
    private PersonaByCurpClient personaByCurpClient;

    @Inject
    @RestClient
    private RegistroBackClient registroBackClient;

    @Override
    public Message<ActualizacionDatosPensionado> execute(Message<ActualizacionDatosPensionado> request) throws BusinessException {
        Response response = null;
        int emailExists = 0;
        try {
            String newEmail = (request.getPayload().getPensionadoDatosNuevos().getCorreoElectronico() == null) ? null : request.getPayload().getPensionadoDatosNuevos().getCorreoElectronico();

            RegistroRequest registroRequest = new RegistroRequest();
            registroRequest.setCorreo(newEmail);
            if(newEmail != null && !newEmail.isEmpty()) {
                response = registroBackClient.validateUser(registroRequest);
                if (!(response != null && response.getStatus() == 200))
                    throw new PersonaPensionadoException(PersonaPensionadoException.ERROR_AL_VALIDAR_USUARIO);
                emailExists = response.readEntity(Integer.class);

                if (emailExists == 1)
                    throw new EMailExistException();
            }

            response = pensionadoClient.updateDatosContacto(Constantes.BEARER + Constantes.ESPACIO_BLANCO +
                request.getPayload().getToken(), request.getPayload());

            if (!(response != null && response.getStatus() == 202))
                throw new PersonaPensionadoException(PersonaPensionadoException.ERROR_AL_ACTUALIZAR_DATOS_PENSIONADO);

            return request;
            
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR::UpdatePensionadoService.execute = {0}", e);
            throw new PersonaPensionadoException(PersonaPensionadoException.ERROR_DESCONOCIDO_DEL_SERVIDOR);
        }
    }
    
}