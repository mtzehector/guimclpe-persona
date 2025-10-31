/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.personafront.service;

import java.util.List;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.PersonaNoRegistradaException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.persona.model.Delegacion;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.PersonaP;
import mx.gob.imss.dpes.personafront.exception.PersonasRQException;
import mx.gob.imss.dpes.personafront.model.PersonasRQ;
import mx.gob.imss.dpes.personafront.restclient.PersonaPersistClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author antonio
 */
@Provider
public class ConsultarPromotorAsignarService extends ServiceDefinition<PersonasRQ, PersonasRQ> {

    @Inject
    @RestClient
    PersonaPersistClient personaService;

    @Override
    public Message<PersonasRQ> execute(Message<PersonasRQ> request) throws BusinessException {
        // Se busca en el sistema de enrolamiento y despues de nuestra base
        log.log(Level.INFO, "ConsultarPersonaService.execute request= {0}", request);

        Response load = personaService.promotoresParaAsignar(
                request.getPayload().getCveDelegacion(),
                request.getPayload().getCveEntidadFinanciera());

        if (load.getStatus() == 200) {
            List<PersonaP> source = load.readEntity(
                    (new GenericType<List<PersonaP>>() {
            }));

            for (PersonaP p : source) {
                log.log(Level.INFO, "PersonaP {0}", p.getCvePersonalEf());

                Response response2 = personaService.delegacionesPorPersona(p.getCvePersonalEf());
                                
                log.log(Level.INFO, "lstDelegaciones {0}", response2);

                List<Delegacion> lstDelegaciones = response2.readEntity(List.class);
                log.log(Level.INFO, "lstDelegaciones {0}", lstDelegaciones);

                p.setLstDelegaciones(lstDelegaciones);
            }

            if (source.isEmpty()) {
                throw new PersonasRQException();
            }
            request.getPayload().setPersonas(source);

            return new Message<>(request.getPayload());

        }
        return response(null, ServiceStatusEnum.EXCEPCION, new PersonaNoRegistradaException(), null);
    }

}
