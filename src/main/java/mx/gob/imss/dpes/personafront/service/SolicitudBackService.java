/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.personafront.service;

import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import mx.gob.imss.dpes.personafront.restclient.SolicitudBackClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author juan.garfias
 */
@Provider
public class SolicitudBackService extends ServiceDefinition<Solicitud, Solicitud> {

    @Inject
    @RestClient
    private SolicitudBackClient client;

    @Override
    public Message<Solicitud> execute(Message<Solicitud> request) throws BusinessException {
        try {
            client.actualizarSolicitudAsignarPromotor(request.getPayload());
        } catch (Exception e) {
        }
        return new Message<>(request.getPayload());
    }
}
