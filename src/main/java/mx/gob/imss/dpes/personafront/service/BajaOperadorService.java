package mx.gob.imss.dpes.personafront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.personafront.model.BajaOperadorRQ;
import mx.gob.imss.dpes.personafront.restclient.PersonaPersistClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author luisr.rodriguez
 */
@Provider
public class BajaOperadorService extends ServiceDefinition<BajaOperadorRQ,BajaOperadorRQ> {
    @Inject
    @RestClient
    private PersonaPersistClient client;

    @Override
    public Message<BajaOperadorRQ> execute(Message<BajaOperadorRQ> request) throws BusinessException {
        Response res = client.bajaOperador(request.getPayload());
        if(res.getStatus() == 200){
            log.log(Level.INFO, "Baja Operador Exitoso", request);
        }else{
            log.log(Level.WARNING, "Baja Operador No Exitoso", request);
        }
        return request;
    }
    
}
