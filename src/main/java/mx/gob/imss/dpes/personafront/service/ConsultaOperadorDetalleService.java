/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.personafront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.PersonaNoRegistradaException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import mx.gob.imss.dpes.interfaces.documento.model.TipoDocumentoFront;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Persona;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.PersonaP;
import mx.gob.imss.dpes.personafront.model.DocumentoRq;
import mx.gob.imss.dpes.personafront.model.DocumentoRs;
import mx.gob.imss.dpes.personafront.model.PersonaEF;
import mx.gob.imss.dpes.personafront.restclient.DocumentoBackClient;
import mx.gob.imss.dpes.personafront.restclient.DocumentoClient;
import mx.gob.imss.dpes.personafront.restclient.DocumentoFrontClient;
import mx.gob.imss.dpes.personafront.restclient.PersonaEFPersistClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import mx.gob.imss.dpes.personafront.restclient.PersonaByCurpClient;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class ConsultaOperadorDetalleService extends ServiceDefinition<Persona, PersonaP> {

    @Inject
    @RestClient
    private PersonaByCurpClient client;

    @Inject
    @RestClient
    private DocumentoClient documentoClient;

    @Inject
    @RestClient
    private PersonaEFPersistClient clientEF;

    @Inject
    //@RestClient
    private DomicilioService domicilioService;

    @Inject
    @RestClient
    private DocumentoBackClient docBackClient;

    @Inject
    @RestClient
    private DocumentoFrontClient docFrontClient;

    @Override
    public Message<PersonaP> execute(Message<Persona> request) throws BusinessException {

        log.log(Level.INFO, ">>> execute(Message<Persona> request): {0}", request.getPayload());

        Response load = client.load(request.getPayload().getCurp());
        if (load.getStatus() == 200) {
            PersonaP source = load.readEntity(PersonaP.class);
            source.setEstadoPersonaEf(source.getCveEstadoPersonaEf() != null ? source.getCveEstadoPersonaEf().getId() : 0L);
            Response create = clientEF.loadPersonaEF(request.getPayload().getCurp());
            PersonaEF personaEF = create.readEntity(PersonaEF.class);
            source.setCveEntidadFederativa(personaEF.getCveDelegacion() != null ? Long.parseLong(personaEF.getCveDelegacion()) : null);
            setDocumentos(source);
            return new Message<>(source);
        }

        return response(null, ServiceStatusEnum.EXCEPCION, new PersonaNoRegistradaException(), null);
    }

    protected void setDocumentos(PersonaP source) {
        log.log(Level.INFO, ">>> ConsultaPersonaDetalleService setDocumentos source: {0}", source);
        Documento documento = new Documento();
        documento.setCvePersona(source.getId());
        documento.setTipoDocumento(TipoDocumentoEnum.IDENTIFICACION_OFICIAL);
        Response respuesta = null;
        respuesta = documentoClient.loadByPersonaAndTipo(documento);
        if (respuesta.getStatus() == 200 && respuesta.hasEntity()) {
            documento = respuesta.readEntity(Documento.class);
            log.log(Level.INFO, ">>> respuesta.readEntity(Documento.class): {0}", documento);

            documento.setTipoDocumentoEnum(new TipoDocumentoFront(TipoDocumentoEnum.IDENTIFICACION_OFICIAL));
            source.setDocumentoIdentOficial(documento);
        }

    }

}
