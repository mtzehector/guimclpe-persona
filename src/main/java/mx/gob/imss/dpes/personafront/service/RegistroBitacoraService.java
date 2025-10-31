package mx.gob.imss.dpes.personafront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.PersonaNoRegistradaException;
import mx.gob.imss.dpes.common.exception.UnknowException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.personafront.model.BitacoraRequest;
import mx.gob.imss.dpes.personafront.restclient.BitacoraBackClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author luisr.rodriguez
 */
@Provider
public class RegistroBitacoraService extends ServiceDefinition<BitacoraRequest,BitacoraRequest> {
    @Inject
    @RestClient
    private BitacoraBackClient client;

    @Override
    public Message<BitacoraRequest> execute(Message<BitacoraRequest> request) throws BusinessException {
        try {
            Response res = client.RegistroCancelacion(request.getPayload());
            if (res.getStatus() == 200) {
                log.log(Level.INFO, ">>> RegistroBitacoraService !!!OK response");
                return new Message<>(request.getPayload());
            }
        }catch(RuntimeException re){
            log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR: RuntimeException message=\n{0}", re.getMessage());
            throw new UnknowException();
        }   
        return response(null, ServiceStatusEnum.EXCEPCION, new PersonaNoRegistradaException(), null);
    }
    
}
