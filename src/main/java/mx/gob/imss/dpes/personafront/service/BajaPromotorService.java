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
import mx.gob.imss.dpes.personafront.model.BajaPromotorRQ;
import mx.gob.imss.dpes.personafront.restclient.PersonaPersistClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author juan.garfias
 */
@Provider
public class BajaPromotorService extends ServiceDefinition<BajaPromotorRQ, BajaPromotorRQ> {

    @Inject
    @RestClient
    private PersonaPersistClient client;
    
    @Override
    public Message<BajaPromotorRQ> execute(Message<BajaPromotorRQ> request) throws BusinessException {

        client.bajaPromotor(request.getPayload());
        
        return request;
    }
}
