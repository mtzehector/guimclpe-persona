package mx.gob.imss.dpes.personafront.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.bitacora.model.BitacoraPensionadoRequest;
import mx.gob.imss.dpes.interfaces.bitacora.model.TipoModificacionRequest;
import mx.gob.imss.dpes.personafront.exception.BitacoraPensionadoException;
import mx.gob.imss.dpes.personafront.model.ActualizacionDatosPensionado;
import mx.gob.imss.dpes.personafront.restclient.BitacoraPensionadoClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import mx.gob.imss.dpes.common.enums.TipoModificacionEnum;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@Provider
public class BitacoraPensionadoService extends ServiceDefinition<ActualizacionDatosPensionado,ActualizacionDatosPensionado>{

    @Inject
    @RestClient
    private BitacoraPensionadoClient client;

    @Override
    public Message<ActualizacionDatosPensionado> execute(Message<ActualizacionDatosPensionado> request) throws BusinessException {
        try {
            BitacoraPensionadoRequest bitacoraPensionadoRequest = new BitacoraPensionadoRequest();
            Long persona = Long.parseLong(request.getPayload().getPensionadoDatosAnteriores().getCvePersona());

            String curp = request.getPayload().getCurp();

            List<BitacoraPensionadoRequest> bitacoraPensionadoRequests = new ArrayList<>();

            if (request.getPayload().getPensionadoDatosNuevos().getCorreoElectronico() != null)
                bitacoraPensionadoRequests.add(create(curp, persona, request.getPayload().getPensionadoDatosAnteriores().getCorreoElectronico(), request.getPayload().getPensionadoDatosNuevos().getCorreoElectronico(), TipoModificacionEnum.CORREO_ELECTRONICO.toValue()));

            if (request.getPayload().getPensionadoDatosNuevos().getTelLocal() != null)
                bitacoraPensionadoRequests.add(create(curp, persona, request.getPayload().getPensionadoDatosAnteriores().getTelLocal(), request.getPayload().getPensionadoDatosNuevos().getTelLocal(), TipoModificacionEnum.TELEFONO_LOCAL.toValue()));

            if (request.getPayload().getPensionadoDatosNuevos().getTelCelular() != null)
                bitacoraPensionadoRequests.add(create(curp, persona, request.getPayload().getPensionadoDatosAnteriores().getTelCelular(), request.getPayload().getPensionadoDatosNuevos().getTelCelular(), TipoModificacionEnum.TELEFONO_CELULAR.toValue()));

            if (!bitacoraPensionadoRequests.isEmpty()) {
                Response response = client.guardarRegistrosActualizados(bitacoraPensionadoRequests);

                if (!(response != null && response.getStatus() == 200))
                    throw new BitacoraPensionadoException(BitacoraPensionadoException.ERROR_AL_GUARDAR_BITACORA_DEL_PENSIONADO);
            }

            return request;
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR::BitacoraPensionadoService.execute = {0}", e);
            throw new BitacoraPensionadoException(BitacoraPensionadoException.ERROR_DESCONOCIDO_DEL_SERVIDOR);
        }
    }

    private BitacoraPensionadoRequest create(String curp, Long persona, String valorAnterior, String valorActual, Long tipoModificacion) {
        BitacoraPensionadoRequest bitacoraPensionadoRequest = new BitacoraPensionadoRequest();
        bitacoraPensionadoRequest.setCveCurp(curp);
        bitacoraPensionadoRequest.setCvePersona(persona);
        bitacoraPensionadoRequest.setValorAnterior(valorAnterior);
        bitacoraPensionadoRequest.setValorActual(valorActual);
        TipoModificacionRequest tipoModificacionRequest = new TipoModificacionRequest();
        tipoModificacionRequest.setCveTipoModificacion(tipoModificacion);
        bitacoraPensionadoRequest.setTipoModificacion(tipoModificacionRequest);
        return bitacoraPensionadoRequest;
    }

}
