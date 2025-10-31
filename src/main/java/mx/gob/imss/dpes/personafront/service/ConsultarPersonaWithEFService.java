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
import mx.gob.imss.dpes.common.enums.TipoPersonaEFEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.PersonaNoRegistradaException;
import mx.gob.imss.dpes.common.exception.UnknowException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.PerfilUsuario;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Persona;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.renapo.model.RenapoCurpIn;
import mx.gob.imss.dpes.interfaces.renapo.model.RenapoCurpOut;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.PersonaP;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.PersonaResponse;
import mx.gob.imss.dpes.interfaces.userdata.model.Usuario;
import mx.gob.imss.dpes.personafront.assembler.PersonaAssembler;
import mx.gob.imss.dpes.personafront.model.DocumentoRq;
import mx.gob.imss.dpes.personafront.model.DocumentoRs;
import mx.gob.imss.dpes.personafront.model.PersonaEF;
import mx.gob.imss.dpes.personafront.restclient.DocumentoBackClient;
import mx.gob.imss.dpes.personafront.restclient.DocumentoFrontClient;
import mx.gob.imss.dpes.personafront.restclient.PerfilUsuarioClient;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import mx.gob.imss.dpes.personafront.restclient.PersonaByCurpClient;
import mx.gob.imss.dpes.personafront.restclient.PersonaEFPersistClient;
import mx.gob.imss.dpes.personafront.restclient.RegistroBackClient;
import mx.gob.imss.dpes.personafront.restclient.RenapoClient;

/**
 *
 * @author antonio
 */
@Provider
public class ConsultarPersonaWithEFService extends ServiceDefinition<Persona, PersonaResponse> {

    @Inject
    @RestClient
    private PersonaByCurpClient personaByCurp;

    @Inject
    @RestClient
    private PersonaEFPersistClient personaEFByCurp;

    @Inject
    private PersonaAssembler personaAssembler;

    @Inject
    @RestClient
    private DocumentoBackClient docBackClient;

    @Inject
    private DocumentoFrontClient docFrontClient;

    @Inject
    @RestClient
    private RenapoClient renapoCli;
    
    @Inject
    @RestClient
    private PerfilUsuarioClient perfilUsuarioClient;
    
    @Inject
    RegistroBackClient regBackCli;
    
    @Override
    public Message<PersonaResponse> execute(Message<Persona> request) throws BusinessException {
        // Se busca en el sistema de enrolamiento y despues de nuestra base
        log.log(Level.INFO, "ConsultarPersonaWithEFService.execute request= {0}", request);
        Response load = null;
        try {
            load = personaByCurp.load(request.getPayload().getCurp());

        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknowException();
        }
        if (load.getStatus() == 200 && load.hasEntity()) {
            PersonaP source = load.readEntity(PersonaP.class);
            PersonaResponse respuesta = personaAssembler.assemble(source);
            return new Message<>(respuesta);
        } else {
            return new Message<>();
        }

        //return response(null, ServiceStatusEnum.EXCEPCION, new PersonaNoRegistradaException(), null);
    }

    public Message<PersonaResponse> validatePersonaWithEF(Persona request, Long cveEntidadFinanciera, Long cveTipoPersona) throws BusinessException {
        log.log(Level.INFO, ">>> ConsultarPersonaWithEFService.validatePersonaWithEF request= {0}", request);
        Response load = null;
        Response loadEF = null;

        try {
            load = personaByCurp.load(request.getCurp());
            loadEF = personaEFByCurp.loadPersonaEF(request.getCurp());

        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknowException();
        }
        PersonaP persona = null;
        if (load.getStatus() == 200 && load.hasEntity()) {
            persona = load.readEntity(PersonaP.class);
            log.log(Level.INFO, ">>> personaFront ConsultarPersonaWithEFService.ValidatePersonaWithEFService persona= {0}", persona);
            if (persona.getCveEntidadFinanciera() != null) {
                if (persona.getCveEntidadFinanciera().compareTo(cveEntidadFinanciera) != 0) {
                    throw new PersonaNoRegistradaException(PersonaNoRegistradaException.PERSONA_EXISTE_NOTHISEF);
                } else {
                    if (loadEF.getStatus() == 200 && loadEF.hasEntity()) {
                        PersonaEF personaEF = loadEF.readEntity(PersonaEF.class);

                        if (cveTipoPersona == 1L) { // Solo para IMAGEN promotor
                            try {
                                Response responseDocBack = docBackClient.load(new DocumentoRq(persona.getId(), 9L));
                                DocumentoRs documento = responseDocBack.readEntity(DocumentoRs.class);

                                Response responseDocFront = docFrontClient.load(documento.getId());
                                documento = responseDocFront.readEntity(DocumentoRs.class);
                                persona.setImgB64(documento.getArchivo());

                            } catch (Exception e) {
                                persona.setImgB64(null);
                            }
                        }
                        if (personaEF.getCveTipoPersonaEf().compareTo(cveTipoPersona) != 0) {
                            if (cveTipoPersona == 1L) {
                                throw new PersonaNoRegistradaException(PersonaNoRegistradaException.PERSONA_EXISTE_NOTHISEF_AS_PROMOTOR);
                            }
                            if (cveTipoPersona == 2L) {
                                throw new PersonaNoRegistradaException(PersonaNoRegistradaException.PERSONA_EXISTE_NOTHISEF_AS_OPERATOR);
                            }
                        }
                    }
                }
            } else {
                if (cveTipoPersona == 2L) {
                    throw new PersonaNoRegistradaException(PersonaNoRegistradaException.PERSONA_EXISTE_NOTHISEF_AS_OPERATOR);
                }

            }

        } else {
            return new Message<>();
        }
        
        PersonaResponse respuesta = personaAssembler.assemble(persona);
        
        Response responseUsuario = regBackCli.obtenerUsuarioByIdPersona(persona.getId());
        if (responseUsuario == null || responseUsuario.getStatus() != 200)
        	throw new PersonaNoRegistradaException(PersonaNoRegistradaException.PERSONA_EXISTE_NOTHISEF);
        
        Usuario usuario = responseUsuario.readEntity(Usuario.class);
        Response response = perfilUsuarioClient.getPerfil(usuario.getId());
        
        if (response == null || response.getStatus() != 200)
        	throw new PersonaNoRegistradaException(PersonaNoRegistradaException.PERSONA_EXISTE_NOTHISEF);
        
        PerfilUsuario perfilUsuario = response.readEntity(PerfilUsuario.class);
        respuesta.setFirmaCartaRecibo(perfilUsuario.getFirmaCartaRecibo());
        
        
        //Response response = renapoCli.load( new RenapoCurpIn( request.getCurp() ) );
        //RenapoCurpOut ro = response.readEntity(RenapoCurpOut.class);
        
        //respuesta.setRenapoOut(ro);
        
        return new Message<>(respuesta);
    }
    
    public Message<PersonaResponse> validatePersonaWithEFExcel(Persona request, Long cveEntidadFinanciera, Long cveTipoPersona) throws BusinessException {
        log.log(Level.INFO, ">>> ConsultarPersonaWithEFService.validatePersonaWithEF request= {0}", request);
        Response load = null;
        Response loadEF = null;

        try {
            load = personaByCurp.load(request.getCurp());
            loadEF = personaEFByCurp.loadPersonaEF(request.getCurp());

        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknowException();
        }
        PersonaP persona = null;
        if (load.getStatus() == 200 && load.hasEntity()) {
            persona = load.readEntity(PersonaP.class);
            log.log(Level.INFO, ">>> personaFront ConsultarPersonaWithEFService.ValidatePersonaWithEFService persona= {0}", persona);
            if (persona.getCveEntidadFinanciera() != null) {
                if (persona.getCveEntidadFinanciera().compareTo(cveEntidadFinanciera) != 0) {
                    throw new PersonaNoRegistradaException(PersonaNoRegistradaException.PERSONA_EXISTE_NOTHISEF);
                } else {
                    if (loadEF.getStatus() == 200 && loadEF.hasEntity()) {
                        PersonaEF personaEF = loadEF.readEntity(PersonaEF.class);

                        if (cveTipoPersona == 1L) { /* Solo para IMAGEN promotor*/}
                        if (personaEF.getCveTipoPersonaEf().compareTo(cveTipoPersona) != 0) {
                            if (cveTipoPersona == 1L) {
                                throw new PersonaNoRegistradaException(PersonaNoRegistradaException.PERSONA_EXISTE_NOTHISEF_AS_PROMOTOR);
                            }
                            if (cveTipoPersona == 2L) {
                                throw new PersonaNoRegistradaException(PersonaNoRegistradaException.PERSONA_EXISTE_NOTHISEF_AS_OPERATOR);
                            }
                        }
                    }
                }
            } else {
                if (cveTipoPersona == 2L) {
                    throw new PersonaNoRegistradaException(PersonaNoRegistradaException.PERSONA_EXISTE_NOTHISEF_AS_OPERATOR);
                }

            }

        } else {
            return new Message<>();
        }
        
        PersonaResponse respuesta = personaAssembler.assemble(persona);
        
        Response response = renapoCli.load( new RenapoCurpIn( request.getCurp() ) );
        RenapoCurpOut ro = response.readEntity(RenapoCurpOut.class);
        
        respuesta.setRenapoOut(ro);
        
        return new Message<>(respuesta);
    }

}
