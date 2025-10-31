package mx.gob.imss.dpes.personafront.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.bitacora.model.BitacoraInterfaz;
import mx.gob.imss.dpes.personafront.restclient.EventoBitacoraClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
public class EventoBitacoraInterfazService extends ServiceDefinition<BitacoraInterfaz, BitacoraInterfaz> {

    @Inject
    @RestClient
    private EventoBitacoraClient eventoBitacoraClient;

    //Este metodo fue creado tolerante a fallos ya que sirve unicamente de forma estadistica.
    //Por lo que no genera excepciones en caso de fallo
    @Override
    public Message<BitacoraInterfaz> execute(Message<BitacoraInterfaz> bitacoraInterfazMessage) throws BusinessException {

        try {
            Response bitacoraInterfazResponse = eventoBitacoraClient.createInterfaz(bitacoraInterfazMessage.getPayload());
            if(bitacoraInterfazResponse != null && bitacoraInterfazResponse.getStatus() == 200)
                return bitacoraInterfazMessage;
            else
                log.log(Level.WARNING, "EventoBitacoraInterfazService.execute bitacoraInterfazMessage = [" +
                        bitacoraInterfazMessage +
                        "] - Hubo un problema con la respuesta del servicio");
        }
        catch (Exception e) {
            log.log(Level.WARNING, "EventoBitacoraInterfazService.execute bitacoraInterfazMessage = [" +
                    bitacoraInterfazMessage + "]", e);
        }

        return bitacoraInterfazMessage;
    }
}
