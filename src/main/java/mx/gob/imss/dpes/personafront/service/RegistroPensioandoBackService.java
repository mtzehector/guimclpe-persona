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
import mx.gob.imss.dpes.personafront.model.UsuarioRQ;
import mx.gob.imss.dpes.personafront.restclient.RegistroBackClient;
import mx.gob.imss.dpes.personafront.restclient.SolicitudBackClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author juan.garfias
 */
@Provider
public class RegistroPensioandoBackService extends ServiceDefinition<UsuarioRQ, UsuarioRQ> {

    @Inject
    @RestClient
    private RegistroBackClient client;

    @Override
    public Message<UsuarioRQ> execute(Message<UsuarioRQ> request) throws BusinessException {
        try {
            client.updateIndActivo(request.getPayload());
        } catch (Exception e) {
        }
        return new Message<>(request.getPayload());
    }
}
