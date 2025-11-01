/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.personafront.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.PersonaNoRegistradaException;
import mx.gob.imss.dpes.common.exception.RegistroPatronalNotBelongsToEntidadFinancieraException;
import mx.gob.imss.dpes.common.exception.UnknowException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import mx.gob.imss.dpes.interfaces.domicilio.model.Domicilio;
import mx.gob.imss.dpes.interfaces.persona.model.DelegacionPersonalEF;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.PersonaP;
import mx.gob.imss.dpes.personafront.model.EntidadFinanciera;
import mx.gob.imss.dpes.personafront.model.PersonaEF;
import mx.gob.imss.dpes.personafront.model.PersonaOrModel;
import mx.gob.imss.dpes.personafront.restclient.DocumentoClient;
import mx.gob.imss.dpes.personafront.restclient.EntidadFinancieraClient;
import mx.gob.imss.dpes.personafront.restclient.PersonaPersistClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import mx.gob.imss.dpes.personafront.restclient.PersonaEFPersistClient;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class PersistePersonaService extends ServiceDefinition<PersonaOrModel, PersonaOrModel> {
    
    @Inject
    @RestClient
    private PersonaPersistClient personaClient;
    
    @Inject
    @RestClient
    private DocumentoClient documentClient;
    
    @Inject
    @RestClient
    private EntidadFinancieraClient clientEntidadFinanciera;
    
    @Inject
    @RestClient
    private PersonaEFPersistClient clientEF;
    
    @Inject
    //@RestClient
    private DomicilioService domicilioService;
    
    protected final static Long CVE_TIPO_PERSONA_EMPLEADO_ENTIDAD = 1L;
    protected final static Long CVE_TIPO_PERSONA_EMPLEADO_TERCERO = 2L;
    protected final static Long CVE_TIPO_PERSONA_ENTIDAD_FIN = 1L;
    protected final static Long CVE_ESTADO_PERSONA_EF = 1L;
    protected final static int INDREGISTRADO = 1;
    
    @Override
    public Message<PersonaOrModel> execute(Message<PersonaOrModel> request) throws BusinessException {
        //request.getPayload().getPersona().setCvePerfil(3L);
        log.log(Level.INFO, ">>> PersistePersonaService.execute persona= {0}", request.getPayload().getPersona());
        Long cveTipoPersonaEF = request.getPayload().getPersona().getCveTipoPersonaEntidadFinanciera();
        Long idEntidadFinanciera = request.getPayload().getPersona().getCveEntidadFinanciera();
        Long previousId = request.getPayload().getPersona().getId();
        if (Objects.isNull(request.getPayload().getPersona().getCvePerfil())) {
            log.log(Level.INFO, ">>>PersistePersonaService Error: persona sin cvePerfil request.getPayload()= {0}", request.getPayload());
            throw new PersonaNoRegistradaException(PersonaNoRegistradaException.CVEPERFIL);
        }
        
        if (request.getPayload().getPersona().getCveTipoEmpleado().compareTo(CVE_TIPO_PERSONA_EMPLEADO_ENTIDAD) == 0) {
            idEntidadFinanciera = getEntidadFinancieraIdByRegistroPatronal(request.getPayload().getPersona().getRegistroPatronal());
            
            if (idEntidadFinanciera != null && idEntidadFinanciera.compareTo(request.getPayload().getPersona().getCveEntidadFinanciera()) == 0) {
                
            } else {
                log.log(Level.INFO, ">>>PersistePersonaService Error: RegistroPatronal =" + request.getPayload().getPersona().getRegistroPatronal() + " NO corresponde a la cveEntidadFinanciera= {0}", idEntidadFinanciera);
                throw new RegistroPatronalNotBelongsToEntidadFinancieraException();
            }
        }
        Domicilio domicilio = null;
        if (request.getPayload().getPersona().getCveTipoEmpleado().compareTo(CVE_TIPO_PERSONA_EMPLEADO_TERCERO) == 0) {
            domicilio = domicilioService.createOrUpdateDomicilio(request.getPayload().getPersona().getDomicilio());
            request.getPayload().getPersona().setDomicilio(domicilio);
            request.getPayload().getPersona().setCveRefDomicilio(domicilio.getIdDomicilio());
            
        }
        request.getPayload().getPersona().setCveEntidadFinanciera(idEntidadFinanciera);
        
        PersonaP personaP = createPersona(request.getPayload().getPersona());
        
        if (personaP != null) {
            personaP.setCveTipoPersonaEntidadFinanciera(cveTipoPersonaEF);
            personaP.setCveEntidadFederativa(request.getPayload().getPersona().getCveEntidadFederativa());
            personaP.setIfe(request.getPayload().getPersona().getIfe());
            personaP.setCartaResponsiva(request.getPayload().getPersona().getCartaResponsiva());
            personaP.setFotografia(request.getPayload().getPersona().getFotografia());
            personaP.setComprobanteDomicilio(request.getPayload().getPersona().getComprobanteDomicilio());
            personaP.setEstadoPersonaEf(request.getPayload().getPersona().getEstadoPersonaEf());
            personaP.setDelegaciones(request.getPayload().getPersona().getDelegaciones());
            //CAUTION: All data from request will be erased!!!!!!
            request.getPayload().setPersona(personaP);
            request.getPayload().getPersona().setDomicilio(domicilio);
            request.getPayload().getPersona().setCveTipoPersonaEntidadFinanciera(cveTipoPersonaEF);
            request.getPayload().getLaboralRequest().setNss(personaP.getNumNss().toString());
            if (personaP.getCveTipoEmpleado().compareTo(CVE_TIPO_PERSONA_EMPLEADO_ENTIDAD) == 0 || personaP.getCveTipoEmpleado().compareTo(CVE_TIPO_PERSONA_EMPLEADO_TERCERO) == 0) {
                updateDocuments(personaP);
                PersonaEF personaEF = createPersonaEF(personaP, previousId);
                personaP.setCvePersonalEf(personaEF.getId());
                personaP = createPersona(personaP);  //Update
                log.log(Level.INFO, ">>> PersistePersonaService.execute final personaP= {0}", personaP);
                
            }
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new PersonaNoRegistradaException(), null);
    }
    
    protected PersonaP createPersona(PersonaP persona) throws BusinessException {
        Response create = null;
        PersonaP personaP = null;
        try {
            create = personaClient.create(persona);
        } catch (Exception e) {
            e.printStackTrace();
            log.log(Level.SEVERE, ">>>ERROR PersistePersonaService message={0}", e.getMessage());
            throw new UnknowException();
        }
        if (create != null && create.getStatus() == 200) {
            personaP = create.readEntity(PersonaP.class);
        }
        return personaP;
    }
    
    private PersonaEF createPersonaEF(PersonaP persona, Long previousId) throws BusinessException {
        PersonaEF personaEF = new PersonaEF();
        if (previousId != null && previousId > 0) {
            Response create = clientEF.loadPersonaEF(persona.getCveCurp());
            personaEF = create.readEntity(PersonaEF.class);
        }
        log.log(Level.SEVERE, "JGV createPersonaEF personaEF.getDelegaciones() {0}:", personaEF.getDelegaciones());

        personaEF.setCveCurp(persona.getCveCurp());
        personaEF.setNumEmpleado(persona.getNumEmpleado());
        personaEF.setCveEntidadFinanciera(persona.getCveEntidadFinanciera());
        personaEF.setCveEstadoPersonaEf(persona.getCveEstadoPersonaEf().getId());
        personaEF.setCveTipoPersonaEf(persona.getCveTipoPersonaEntidadFinanciera());
        personaEF.setCveDelegacion(persona.getCveEntidadFederativa() != null ? persona.getCveEntidadFederativa().toString() : null);
        personaEF.setIndRegistrado(INDREGISTRADO);
        log.log(Level.SEVERE, "JGV createPersonaEF persona.getDelegaciones() 2 {0}:", persona.getDelegaciones());
        personaEF.setDelegaciones(persona.getDelegaciones());
        
        Response createdEF = null;
        for (DelegacionPersonalEF delegacionPersonalEF : persona.getDelegaciones()) {
            if (delegacionPersonalEF.getCveDelegacion() == null) {
                log.log(Level.SEVERE, ">>> registroFront PersistePersonaService.createPersonaEF cveDelegacion==null", personaEF);
                throw new UnknowException();
            }
        }
        try {
            log.log(Level.INFO, ">>> registroFront PersistePersonaService.createPersonaEF personaEF= {0}", personaEF);
            createdEF = clientEF.create(personaEF);
            personaEF = createdEF.readEntity(PersonaEF.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknowException();
        }
        log.log(Level.INFO, ">>> PersistePersonaService.createPersonaEF createdEF= {0}", createdEF);
        return personaEF;
        
    }
    
    private Long getEntidadFinancieraIdByRegistroPatronal(String registroPatronal) throws BusinessException {
        log.log(Level.INFO, ">>> PersistePersonaService.getEntidadFinancieraIdByRegistroPatronal registroPatronal= {0}", registroPatronal);
        Long id = null;
        Response load = null;
        try {
            load = clientEntidadFinanciera.load(registroPatronal);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknowException();
        }
        log.log(Level.INFO, ">>> ****PersistePersonaService.getEntidadFinancieraIdByRegistroPatronal getLength= {0}", load.getLength());
        log.log(Level.INFO, ">>> ****PersistePersonaService.getEntidadFinancieraIdByRegistroPatronal getStringHeaders= {0}", load.getStringHeaders());
        log.log(Level.INFO, ">>> ****PersistePersonaService.getEntidadFinancieraIdByRegistroPatronal hasEntity= {0}", load.hasEntity());
        if (load.getLength() == 0 || load.hasEntity() == false) {
            
            load = clientEntidadFinanciera.loadAso(registroPatronal); 
            if (load.getLength() == 0 || load.hasEntity() == false) {
                throw new PersonaNoRegistradaException(PersonaNoRegistradaException.REGENTIDAD);
            }
        }
        if (load.getStatus() == 200) {
            EntidadFinanciera source = load.readEntity(EntidadFinanciera.class);
            id = source.getId();
        }
        log.log(Level.INFO, ">>> PersistePersonaService.getEntidadFinancieraIdByRegistroPatronal id= {0}", id);
        return id;
    }
    
    private void updateDocuments(PersonaP persona) throws BusinessException {
        Documento ife = persona.getIfe();
        ife.setCvePersona(persona.getId());
        Documento cartaResponsiva = persona.getCartaResponsiva();
        cartaResponsiva.setCvePersona(persona.getId());
        Documento fotografia = persona.getFotografia();
        fotografia.setCvePersona(persona.getId());
        List<Documento> documentoList = new LinkedList<Documento>();
        documentoList.add(ife);
        documentoList.add(cartaResponsiva);
        documentoList.add(fotografia);
        if (persona.getCveTipoEmpleado().compareTo(PersistePersonaService.CVE_TIPO_PERSONA_EMPLEADO_TERCERO) == 0) {
            Documento comprobanteDomicilio = persona.getComprobanteDomicilio();
            comprobanteDomicilio.setCvePersona(persona.getId());
            documentoList.add(comprobanteDomicilio);
        }
        Response respuesta = null;
        respuesta = documentClient.updatePersonaClean(ife);
        if (respuesta.getStatus() == 200) {
            for (Documento documento : documentoList) {
                respuesta = documentClient.updatePersona(documento);
                if (respuesta.getStatus() == 200) {
                    documento = respuesta.readEntity(Documento.class);
                    log.log(Level.INFO, ">>>PersistePersonaService updateDocuments [" + documento.getTipoDocumento() + "]= {0}", documento);
                    
                } else {
                    log.log(Level.SEVERE, ">>>PersistePersonaService ERROR updateDocuments [" + documento.getTipoDocumento() + "]= {0}", documento);
                    throw new UnknowException();
                }
            }
        }
        
    }
    
    public PersonaP updatePersona(PersonaP persona) throws BusinessException {
        personaClient.update(persona);
        return persona;
    }
    
    public PersonaP personaById(PersonaP persona) throws BusinessException {
        Response r = personaClient.personaById(persona.getId());
        return r.readEntity(PersonaP.class);
    }
    
}
