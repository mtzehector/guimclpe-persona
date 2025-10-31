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
import mx.gob.imss.dpes.personafront.model.Pensionado;
import mx.gob.imss.dpes.personafront.restclient.PensionadoClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 * Constula Pensionado
 * @author luisr.rodriguez
 */
@Provider
public class ReadPensionadoService extends ServiceDefinition<Pensionado, Pensionado> {
    
    @Inject
    @RestClient
    private PensionadoClient pensionadoClient;

    @Override
    public Message<Pensionado> execute(Message<Pensionado> request) throws BusinessException {
        log.log(Level.INFO, ">>> personaFront ReadPensionadoService.execute: {0}"
                ,"curp: " + request.getPayload().getCveCurp() 
                + " - nss: " + request.getPayload().getNumNss());
        Response response = null;
        try{
            response = pensionadoClient.getByCurpOrNss(request.getPayload().getCveCurp()
                    ,request.getPayload().getNumNss());
        }
        catch(Exception e){
            throw new UnknowException();
        }
        if (response.getStatus() == 200) {
            Pensionado source = response.readEntity(Pensionado.class);
            return new Message<>(source);
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new PersonaNoRegistradaException(), null);
    }
    
}