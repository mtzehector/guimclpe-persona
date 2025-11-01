/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.personafront.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import mx.gob.imss.dpes.personafront.exception.BitacoraException;
import mx.gob.imss.dpes.personafront.exception.BitacoraPensionadoException;
import mx.gob.imss.dpes.personafront.exception.CancelacionSolicitudException;
import mx.gob.imss.dpes.personafront.model.ActualizacionDatosPensionado;
import mx.gob.imss.dpes.personafront.model.BitacoraRequest;
import mx.gob.imss.dpes.personafront.model.CancelaSolicitudRequest;
import mx.gob.imss.dpes.personafront.model.CancelaSolicitudResponse;
import mx.gob.imss.dpes.personafront.restclient.BitacoraBackClient;
import mx.gob.imss.dpes.personafront.restclient.CancelaSolicitudClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author juanf.barragan
 */
@Provider
public class CancelaSolicitudService extends ServiceDefinition<ActualizacionDatosPensionado,ActualizacionDatosPensionado>{
    
    @Inject
    @RestClient
    CancelaSolicitudClient client;
    
    @Inject
    @RestClient
    BitacoraBackClient bbClient;

    @Override
    public Message<ActualizacionDatosPensionado> execute(Message<ActualizacionDatosPensionado> request) throws BusinessException {
        try {
            CancelaSolicitudRequest cs = new CancelaSolicitudRequest();

            cs.setNss(request.getPayload().getPensionadoDatosAnteriores().getNumNss());

            Response response = client.CancelaSolicitudes(cs);

            if (!(response != null && response.getStatus() == 200))
                throw new CancelacionSolicitudException(CancelacionSolicitudException.ERROR_AL_CANCELAR_SOLICITUD_DEL_PENSIONADO);
                //log.log(Level.INFO, "Cancelación correcta de solicitudes del nss {0}", cs);

            CancelaSolicitudResponse res = response.readEntity(CancelaSolicitudResponse.class);
            if (res.getListaCancelados().size() > 0) {
                for (Solicitud sol : res.getListaCancelados()) {
                    BitacoraRequest rq = new BitacoraRequest();
                    rq.setCurp(request.getPayload().getCurp());
                    rq.setEstadoSolicitud(6L);
                    rq.setIdSolicitud(sol.getId());
                    rq.setSesion(Long.parseLong(request.getPayload().getNumSesion()));
                    rq.setTipo(26L);

                    Response respuesta = bbClient.RegistroCancelacion(rq);

                    if (!(respuesta != null && respuesta.getStatus() == 200))
                        throw new BitacoraException(BitacoraException.ERROR_AL_GUARDAR_EN_BITACORA);

                }
            }
            return request;

        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR::CancelaSolicitudService.execute = {0}", e);
            throw new CancelacionSolicitudException(CancelacionSolicitudException.ERROR_DESCONOCIDO_DEL_SERVIDOR);
        }

    }
    
    
}
